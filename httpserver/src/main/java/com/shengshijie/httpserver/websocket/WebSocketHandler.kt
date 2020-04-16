package com.shengshijie.httpserver.websocket

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.websocketx.*
import io.netty.util.AttributeKey

class WebSocketHandler : SimpleChannelInboundHandler<WebSocketRequestVo>() {
    override fun channelInactive(ctx: ChannelHandlerContext) {
        val id = ctx.channel().attr(SOCKET_ID_ATTR).get() ?: return
        WsClientManager.removeChannel(id)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, requestVo: WebSocketRequestVo) {
        if (requestVo.request != null) {
            handleShake(ctx, requestVo.request)
        }
        if (requestVo.frame != null) {
            handleFrame(ctx, requestVo.frame)
        }
    }

    private fun handleShake(ctx: ChannelHandlerContext, request: FullHttpRequest?) {
        val wsFactory = WebSocketServerHandshakerFactory(null, null, false)
        val handshaker = wsFactory.newHandshaker(request)
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel())
        } else {
            handshaker.handshake(ctx.channel(), request)
            val params = ParamUtil.getRequestParams(request)
            val id = params["userId"]
            WsClientManager.putChannel(id, ctx.channel())
            ctx.channel().attr(HAND_SHAKE_ATTR).set(handshaker)
            ctx.channel().attr(SOCKET_ID_ATTR).set(id)
        }
    }

    private fun handleFrame(ctx: ChannelHandlerContext, frame: WebSocketFrame?) {
        if (frame is CloseWebSocketFrame) {
            val handshaker = ctx.channel().attr(HAND_SHAKE_ATTR).get()
            if (handshaker == null) {
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
                return
            }
            handshaker.close(ctx.channel(), frame.retain() as CloseWebSocketFrame)
            return
        }
        if (frame is PingWebSocketFrame) {
            ctx.channel().writeAndFlush(PongWebSocketFrame(frame.content().retain()))
            return
        }
        if (frame !is TextWebSocketFrame) {
            throw UnsupportedOperationException("暂不支持该消息类型：" + frame!!.javaClass.name)
        }
        val msg = frame.text()
        WsClientManager.handleMsg(msg)
    }

    companion object {
        private val HAND_SHAKE_ATTR = AttributeKey.valueOf<WebSocketServerHandshaker>("HAND_SHAKE")
        private val SOCKET_ID_ATTR = AttributeKey.valueOf<String>("SOCKET_ID")
    }
}