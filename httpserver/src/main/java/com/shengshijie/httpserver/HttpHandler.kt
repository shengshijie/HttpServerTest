package com.shengshijie.httpserver

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.util.ReferenceCountUtil
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.Executors

@Sharable
class HttpHandler : SimpleChannelInboundHandler<FullHttpRequest>() {

    private val functionHandlerMap = HashMap<Path, Invoker>()
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
        val invoker: Invoker?
        return try {
            invoker = matchFunctionHandler(request)
            val rawResponse = invoker?.method?.invoke(invoker.any, request)
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
        }
    }

    fun registerRouter() {
        for (handler in ServiceLoader.load(IController::class.java)) {
            if (handler.javaClass.isAnnotationPresent(Controller::class.java)) {
                val controllerAnnotation = handler.javaClass.getAnnotation(Controller::class.java)!!
                val methods: Array<Method> = handler.javaClass.declaredMethods
                methods.forEach {
                    if (it.isAnnotationPresent(RequestMapping::class.java)) {
                        val requestMappingAnnotation = it.getAnnotation(RequestMapping::class.java)!!
                        val path: Path? = Path.make(requestMappingAnnotation, controllerAnnotation)
                        if (path != null && !functionHandlerMap.containsKey(path)) {
                            functionHandlerMap[path] = Invoker(it, handler.javaClass.newInstance())
                        }
                    }
                }
            }
        }
    }

    @Throws(PathNotFoundException::class, MethodNotAllowedException::class)
    private fun matchFunctionHandler(request: HttpRequest): Invoker? {
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