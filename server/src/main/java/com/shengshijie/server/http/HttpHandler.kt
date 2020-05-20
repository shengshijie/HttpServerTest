package com.shengshijie.server.http

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.ChannelHolder.set
import com.shengshijie.server.http.ChannelHolder.unset
import com.shengshijie.server.http.utils.ExceptionUtils
import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.*
import io.netty.handler.timeout.IdleStateEvent
import io.netty.util.ReferenceCountUtil
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor


@Sharable
class HttpHandler : ChannelInboundHandlerAdapter() {

    private val executor: ThreadPoolExecutor? = TracingThreadPoolExecutor(ServerManager.mServerConfig.corePoolSize,
            ServerManager.mServerConfig.maximumPoolSize,
            LinkedBlockingQueue(ServerManager.mServerConfig.workQueueCapacity))

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun channelRead(ctx: ChannelHandlerContext, any: Any) {
        ServerManager.mStatus.totalRequestsIncrement()
        if (executor == null) {
            handleHttpRequest(ctx, any)
            return
        }
        executor.execute {
            handleHttpRequest(ctx, any)
        }
    }

    private fun handleHttpRequest(ctx: ChannelHandlerContext, any: Any) {
        val request = HttpRequestImpl(any as FullHttpRequest)
        val response = HttpResponseImpl(DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK))
        try {
            set(ctx.channel())
            ServerManager.mRouterManager.matchRouter(request).call(request, response)
        } catch (e: Exception) {
            ServerManager.mLogManager.e("server error: ${ExceptionUtils.toString(e)}")
            response.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR)
            ByteBufUtil.writeUtf8(response.content(), "http handler error: ${e.message}")
        } finally {
            unset()
            HttpUtil.setContentLength(response.fullHttpResponse, response.content().readableBytes().toLong())
            ctx.channel().writeAndFlush(response.fullHttpResponse)?.addListener(ChannelFutureListener.CLOSE)
            ReferenceCountUtil.release(any)
            ServerManager.mStatus.handledRequestsIncrement()
        }
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any?) {
        if (evt is IdleStateEvent && evt === IdleStateEvent.READER_IDLE_STATE_EVENT) {
            ctx.channel().close()
        }
        super.userEventTriggered(ctx, evt)
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        ServerManager.mStatus.connectionIncrement()
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        ServerManager.mStatus.connectionDecrement()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        ServerManager.mLogManager.e("http handler exception: ${ExceptionUtils.toString(cause)}")
        ctx.channel().close()
    }

}