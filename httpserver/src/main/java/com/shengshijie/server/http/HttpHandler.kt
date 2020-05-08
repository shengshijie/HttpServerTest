package com.shengshijie.server.http

import com.shengshijie.server.http.controller.Status
import com.shengshijie.server.http.exception.MethodNotAllowedException
import com.shengshijie.server.http.exception.PathNotFoundException
import com.shengshijie.server.http.router.Invoker
import com.shengshijie.server.http.router.RouterManager
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

@Sharable
class HttpHandler : SimpleChannelInboundHandler<Any>() {

    private val executor = TracingThreadPoolExecutor(100, 100, LinkedBlockingQueue(10000))

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun channelRead0(ctx: ChannelHandlerContext, any: Any) {
        Status.totalRequestsIncrement()
        val request: FullHttpRequest? = any as? FullHttpRequest
        request?.copy()?.let {
            executor.execute {
                val response = handleHttpRequest(request)
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
            }
        }
    }

    private fun handleHttpRequest(fullRequest: FullHttpRequest): FullHttpResponse {
        val invoker: Invoker?
        return try {
            invoker = RouterManager.matchRouter(fullRequest)
            val rawResponse = invoker?.method?.invoke(invoker.any, fullRequest)
            if (rawResponse is RawResponse<*>) {
                HttpResponse.ok(rawResponse.toJSONString())
            } else {
                HttpResponse.make(HttpResponseStatus.INTERNAL_SERVER_ERROR)
            }
        } catch (error: MethodNotAllowedException) {
            HttpResponse.make(HttpResponseStatus.METHOD_NOT_ALLOWED)
        } catch (error: PathNotFoundException) {
            HttpResponse.make(HttpResponseStatus.NOT_FOUND)
        } catch (error: Exception) {
            HttpResponse.makeError(error)
        } finally {
            Status.handledRequestsIncrement()
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        Status.connectionIncrement()
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        Status.connectionDecrement()
    }

}