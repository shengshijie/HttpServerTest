package com.shengshijie.httpserver.websocket;

import java.util.Map;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;

public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketRequestVo> {

    private static final AttributeKey<WebSocketServerHandshaker> HAND_SHAKE_ATTR = AttributeKey.valueOf("HAND_SHAKE");
    private static final AttributeKey<String> SOCKET_ID_ATTR = AttributeKey.valueOf("SOCKET_ID");

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String id = ctx.channel().attr(SOCKET_ID_ATTR).get();
        if(id == null){
            return;
        }
        WsClientManager.getInstance().removeChannel(id);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketRequestVo requestVo) {
        if(requestVo.getRequest() != null){
            this.handleShake(ctx, requestVo.getRequest());
        }
        if(requestVo.getFrame() != null){
            this.handleFrame(ctx, requestVo.getFrame());
        }
    }

    private void handleShake(ChannelHandlerContext ctx, FullHttpRequest request){
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(null, null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), request);
            Map<String, String> params = ParamUtil.getRequestParams(request);
            String id = params.get("userId");
            WsClientManager.getInstance().putChannel(id, ctx.channel());
            ctx.channel().attr(HAND_SHAKE_ATTR).set(handshaker);
            ctx.channel().attr(SOCKET_ID_ATTR).set(id);
        }
    }

    private void handleFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        if (frame instanceof CloseWebSocketFrame) {
            WebSocketServerHandshaker handshaker = ctx.channel().attr(HAND_SHAKE_ATTR).get();
            if(handshaker == null){
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                return;
            }
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (! (frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException("暂不支持该消息类型：" + frame.getClass().getName());
        }
        String msg = ((TextWebSocketFrame) frame).text();
        WsClientManager.getInstance().handleMsg(msg);
    }

}
