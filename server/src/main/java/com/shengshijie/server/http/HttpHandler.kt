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
        val request: FullHttpRequest? = any as? FullHttpRequest
        request?.let {
            executor.execute {
                ctx.writeAndFlush(handleHttpRequest(ctx, request)).addListener(ChannelFutureListener.CLOSE)
                ReferenceCountUtil.release(request)
            }
        }
    }

    private fun handleHttpRequest(ctx: ChannelHandlerContext, fullRequest: FullHttpRequest): FullHttpResponse {
        val invoker: Invoker?
        return try {
            set(ctx.channel())
            val parameterMap = HttpRequestUtil.getParameterMap(fullRequest)
            var args = arrayListOf<Any>()
            invoker = ServerManager.mRouterManager.matchRouter(fullRequest)
            invoker?.args?.let {
                args = arrayListOf()
                for (i in 1 until it.size) {
                    parameterMap[it[i].name]?.apply {
                        args.add(this[0])
                    }
                }
            }
            val argsArray = args.toArray()
            val rawResponse = invoker?.method?.call(invoker.instance, *argsArray)
            if (rawResponse is RawResponse<*>) {
                HttpResponse.ok(rawResponse.toJSONString())
            } else {
                HttpResponse.make(HttpResponseStatus.INTERNAL_SERVER_ERROR)
            }
        } catch (error: MethodNotAllowedException) {
            LogManager.e(ExceptionUtils.toString(error))
            HttpResponse.make(HttpResponseStatus.METHOD_NOT_ALLOWED)
        } catch (error: PathNotFoundException) {
            LogManager.e(ExceptionUtils.toString(error))
            HttpResponse.make(HttpResponseStatus.NOT_FOUND)
        } catch (error: Exception) {
            LogManager.e(ExceptionUtils.toString(error))
            HttpResponse.makeError(error)
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
        LogManager.e(ExceptionUtils.toString(cause))
        ctx.channel().close()
    }

}