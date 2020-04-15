package com.shengshijie.httpserver.websocket;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

public class ResponseUtil {

    public static FullHttpResponse get400Response(){
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
    }

    public static FullHttpResponse get200Response(String content){
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content.getBytes()));
    }

    public static FullHttpResponse get500Response(){
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer("服务器异常".getBytes()));
    }

    public static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        if (response.status().code() != 200) {
            ByteBufUtil.writeUtf8(response.content(), response.status().toString());
        }
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
        response.headers().add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS,"*");//允许headers自定义
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS,"GET, POST, PUT,DELETE");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS,"true");
        if (! HttpUtil.isKeepAlive(request) || response.status().code() != 200) {
            response.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }else{
            response.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.channel().writeAndFlush(response);
        }
    }

}