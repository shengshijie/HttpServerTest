package com.shengshijie.httpserver;

import com.shengshijie.httpserver.file.FileUploadRequestVo;
import com.shengshijie.httpserver.http.HttpRequestVo;
import com.shengshijie.httpserver.websocket.ParamUtil;
import com.shengshijie.httpserver.websocket.WebSocketRequestVo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class DispatchHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            if(isWebSocketHandShake(request)) {
                ctx.fireChannelRead(new WebSocketRequestVo(request));
            }else if(isFileUpload(request)){
                ctx.fireChannelRead(new FileUploadRequestVo(request));
            }else{
                ctx.fireChannelRead(new HttpRequestVo(request));
            }
        } else if (msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            ctx.fireChannelRead(new WebSocketRequestVo(frame));
        }
    }

    private boolean isWebSocketHandShake(FullHttpRequest request){
        return request.method().equals(HttpMethod.GET)
                && request.headers().contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true)
                && request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true);
    }

    private boolean isFileUpload(FullHttpRequest request){
        String uri = ParamUtil.getUri(request);
        String contentType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if(contentType == null || contentType.isEmpty()){
            return false;
        }
        return "/upload".equals(uri)
                && request.method() == HttpMethod.POST
                && contentType.toLowerCase().contains(HttpHeaderValues.MULTIPART_FORM_DATA);
    }

}