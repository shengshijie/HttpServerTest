package com.shengshijie.server.http

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.ChannelHolder.set
import com.shengshijie.server.http.ChannelHolder.unset
import com.shengshijie.server.http.exception.MethodNotAllowedException
import com.shengshijie.server.http.exception.PathNotFoundException
import com.shengshijie.server.http.router.Invoker
import com.shengshijie.server.http.utils.ExceptionUtils
import com.shengshijie.server.http.utils.HttpRequestUtil
import com.shengshijie.server.log.LogManager
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.timeout.IdleStateEvent
import io.netty.util.ReferenceCountUtil
import java.util.concurrent.LinkedBlockingQueue

@Sharable
class HttpHandler : ChannelInboundHandlerAdapter() {

    private val executor = TracingThreadPoolExecutor(ServerManager.mServerConfig.corePoolSize,
            ServerManager.mServerConfig.maximumPoolSize,
            LinkedBlockingQueue(ServerManager.mServerConfig.workQueueCapacity))

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun channelRead(ctx: ChannelHandlerContext, any: Any) {
        ServerManager.mStatus.totalRequestsIncrement()
        executor.execute {
            ctx.writeAndFlush(handleHttpRequest(ctx, any)).addListener(ChannelFutureListener.CLOSE)
            ReferenceCountUtil.release(any)
        }
    }

    private fun handleHttpRequest(ctx: ChannelHandlerContext, any: Any): FullHttpResponse {
        val invoker: Invoker?
        return try {
            set(ctx.channel())
            val request: FullHttpRequest = any as FullHttpRequest
            val parameterMap = HttpRequestUtil.getParameterMap(request)
            val args = arrayListOf<Any>()
            invoker = ServerManager.mRouterManager.matchRouter(request)
            invoker?.args?.let {
                for (i in 1 until it.size) {
                    parameterMap[it[i].name]?.apply {
                        args.add(this[0])
                    }
                }
            }
            val rawResponse = invoker?.method?.call(invoker.instance, *(args.toArray()))
            if (rawResponse is RawResponse<*>) {
                HttpResponse.ok(ServerManager.mSerialize.serialize(rawResponse))
            } else {
                HttpResponse.make(HttpResponseStatus.INTERNAL_SERVER_ERROR)
            }
        } catch (e: MethodNotAllowedException) {
            LogManager.e("http handler error: ${ExceptionUtils.toString(e)}")
            HttpResponse.make(HttpResponseStatus.METHOD_NOT_ALLOWED)
        } catch (e: PathNotFoundException) {
            LogManager.e("http handler error: ${ExceptionUtils.toString(e)}")
            HttpResponse.make(HttpResponseStatus.NOT_FOUND)
        } catch (e: Exception) {
            LogManager.e("http handler error: ${ExceptionUtils.toString(e)}")
            HttpResponse.makeError(e)
        } finally {
            unset()
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
        LogManager.e("http handler exception: ${ExceptionUtils.toString(cause)}")
        ctx.channel().close()
    }

}