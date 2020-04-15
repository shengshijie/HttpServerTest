package com.shengshijie.httpserver.http

import com.shengshijie.httpserver.websocket.ParamUtil
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
class HttpHandler : SimpleChannelInboundHandler<HttpRequestVo>() {

    private val functionHandlerMap = HashMap<Path, Invoker>()
    private val executor = Executors.newCachedThreadPool { runnable: Runnable ->
        val thread = Executors.defaultThreadFactory().newThread(runnable)
        thread.name = "netty-" + thread.name
        thread
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun channelRead0(ctx: ChannelHandlerContext, requestVo: HttpRequestVo) {
        val request: FullHttpRequest = requestVo.getRequest()
        val uri = ParamUtil.getUri(request)
        val copyRequest = request.copy()
        executor.execute { onReceivedRequest(ctx, HttpFullRequest(copyRequest)) }
    }

    private fun onReceivedRequest(context: ChannelHandlerContext, fullRequest: HttpFullRequest) {
        val response = handleHttpRequest(fullRequest)
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        ReferenceCountUtil.release(fullRequest)
    }

    private fun handleHttpRequest(fullRequest: HttpFullRequest): FullHttpResponse {
        val invoker: Invoker?
        return try {
            invoker = matchFunctionHandler(fullRequest)
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
        }
    }

    fun registerRouter() {
        for (handler in ServiceLoader.load(IController::class.java)) {
            if (handler.javaClass.isAnnotationPresent(Controller::class.java) && handler.javaClass.isAnnotationPresent(RequestMapping::class.java)) {
                val rootRequestMappingAnnotation = handler.javaClass.getAnnotation(RequestMapping::class.java)!!
                val methods: Array<Method> = handler.javaClass.declaredMethods
                methods.forEach {
                    if (it.isAnnotationPresent(RequestMapping::class.java)) {
                        val requestMappingAnnotation = it.getAnnotation(RequestMapping::class.java)!!
                        val path: Path? = Path.make(requestMappingAnnotation, rootRequestMappingAnnotation)
                        if (path != null && !functionHandlerMap.containsKey(path)) {
                            functionHandlerMap[path] = Invoker(it, handler.javaClass.newInstance())
                        }
                    }
                }
            }
        }
    }

    @Throws(PathNotFoundException::class, MethodNotAllowedException::class)
    private fun matchFunctionHandler(fullRequest: HttpFullRequest): Invoker? {
        var requestPath: Path? = null
        var methodAllowed = false
        for ((path) in functionHandlerMap) {
            if (fullRequest.matched(path.uri, path.isEqual)) {
                requestPath = path
                if (fullRequest.isAllowed(path.method)) {
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