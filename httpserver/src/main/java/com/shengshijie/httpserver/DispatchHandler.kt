package com.shengshijie.httpserver

import com.shengshijie.httpserver.file.FileUploadRequestVo
import com.shengshijie.httpserver.http.HttpRequestVo
import com.shengshijie.httpserver.websocket.ParamUtil
import com.shengshijie.httpserver.websocket.WebSocketRequestVo
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import java.util.*

class DispatchHandler : SimpleChannelInboundHandler<Any?>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any?) {
        if (msg is FullHttpRequest) {
            when {
                isWebSocketHandShake(msg) -> {
                    ctx.fireChannelRead(WebSocketRequestVo(msg))
                }
                isFileUpload(msg) -> {
                    ctx.fireChannelRead(FileUploadRequestVo(msg))
                }
                else -> {
                    ctx.fireChannelRead(HttpRequestVo(msg))
                }
            }
        } else if (msg is WebSocketFrame) {
            ctx.fireChannelRead(WebSocketRequestVo(msg))
        }
    }

    private fun isWebSocketHandShake(request: FullHttpRequest): Boolean {
        return (request.method() == HttpMethod.GET && request.headers().contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true)
                && request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true))
    }

    private fun isFileUpload(request: FullHttpRequest): Boolean {
        val uri = ParamUtil.getUri(request)
        val contentType = request.headers()[HttpHeaderNames.CONTENT_TYPE]
        return if (contentType == null || contentType.isEmpty()) {
            false
        } else "/upload" == uri && request.method() === HttpMethod.POST && contentType.toLowerCase(Locale.getDefault()).contains(HttpHeaderValues.MULTIPART_FORM_DATA)
    }

}