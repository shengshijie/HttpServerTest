package com.shengshijie.httpserver

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.util.ReferenceCountUtil
import java.util.*
import java.util.concurrent.Executors

@Sharable
class HttpHandler : SimpleChannelInboundHandler<FullHttpRequest>() {

    private val functionHandlerMap = HashMap<Path, IFunctionHandler<*>>()
    private val executor = Executors.newCachedThreadPool { runnable: Runnable ->
        val thread = Executors.defaultThreadFactory().newThread(runnable)
        thread.name = "netty-" + thread.name
        thread
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun channelRead0(ctx: ChannelHandlerContext, request: FullHttpRequest) {
        val copyRequest = request.copy()
        executor.execute { onReceivedRequest(ctx, HttpRequest(copyRequest)) }
    }

    private fun onReceivedRequest(context: ChannelHandlerContext, request: HttpRequest) {
        val response = handleHttpRequest(request)
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        ReferenceCountUtil.release(request)
    }

    private fun handleHttpRequest(request: HttpRequest): FullHttpResponse {
        val functionHandler: IFunctionHandler<*>?
        return try {
            functionHandler = matchFunctionHandler(request)
            val rawResponse = functionHandler?.execute(request)
            HttpResponse.ok(rawResponse?.toJSONString())
        } catch (error: MethodNotAllowedException) {
            HttpResponse.make(HttpResponseStatus.METHOD_NOT_ALLOWED)
        } catch (error: PathNotFoundException) {
            HttpResponse.make(HttpResponseStatus.NOT_FOUND)
        } catch (error: Exception) {
            HttpResponse.makeError(error)
        }
    }

    fun registerRouter() {
        for (handler in ServiceLoader.load(IFunctionHandler::class.java)) {
            val path: Path? = handler.javaClass.getAnnotation(RequestMapping::class.java)?.let { Path.make(it) }
            if (path != null && !functionHandlerMap.containsKey(path)) {
                functionHandlerMap[path] = handler
            }
        }
    }

    @Throws(PathNotFoundException::class, MethodNotAllowedException::class)
    private fun matchFunctionHandler(request: HttpRequest): IFunctionHandler<*>? {
        var requestPath: Path? = null
        var methodAllowed = false
        for ((path) in functionHandlerMap) {
            if (request.matched(path.uri, path.isEqual)) {
                requestPath = path
                if (request.isAllowed(path.method)) {
                    methodAllowed = true
                }
            }
        }
        if (requestPath == null) {
            throw PathNotFoundException()
        }
        if (!methodAllowed) {
            throw MethodNotAllowedException()
        }
        return functionHandlerMap[requestPath]
    }
}