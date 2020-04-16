package com.shengshijie.httpserver.websocket

import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.*

object ResponseUtil {
    fun get400Response(): FullHttpResponse {
        return DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST)
    }

    fun get200Response(content: String): FullHttpResponse {
        return DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content.toByteArray()))
    }

    fun get500Response(): FullHttpResponse {
        return DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer("服务器异常".toByteArray()))
    }

    fun sendHttpResponse(ctx: ChannelHandlerContext, request: FullHttpRequest?, response: FullHttpResponse?) {
        if (response!!.status().code() != 200) {
            ByteBufUtil.writeUtf8(response.content(), response.status().toString())
        }
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
        response.headers().add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())
        response.headers()[HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN] = "*"
        response.headers()[HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS] = "*" //允许headers自定义
        response.headers()[HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS] = "GET, POST, PUT,DELETE"
        response.headers()[HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS] = "true"
        if (!HttpUtil.isKeepAlive(request) || response.status().code() != 200) {
            response.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
            ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
        } else {
            response.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
            ctx.channel().writeAndFlush(response)
        }
    }
}