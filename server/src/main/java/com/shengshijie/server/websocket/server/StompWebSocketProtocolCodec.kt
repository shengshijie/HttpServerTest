package com.shengshijie.server.websocket.server

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete
import io.netty.handler.codec.stomp.StompSubframeAggregator
import io.netty.handler.codec.stomp.StompSubframeDecoder
import io.netty.handler.codec.stomp.StompSubframeEncoder

@Sharable
class StompWebSocketProtocolCodec : MessageToMessageCodec<WebSocketFrame, ByteBuf>() {
    private val stompChatHandler = StompChatHandler()

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is HandshakeComplete) {
            val stompVersion: StompVersion = StompVersion.findBySubProtocol(evt.selectedSubprotocol())
            ctx.channel().attr(StompVersion.CHANNEL_ATTRIBUTE_KEY).set(stompVersion)
            ctx.pipeline()
                    .addLast(StompSubframeDecoder())
                    .addLast(StompSubframeEncoder())
                    .addLast(StompSubframeAggregator(65536))
                    .addLast(stompChatHandler)
                    .remove(StompWebSocketClientPageHandler.Companion.INSTANCE)
        } else {
            super.userEventTriggered(ctx, evt)
        }
    }

    override fun encode(ctx: ChannelHandlerContext, stompFrame: ByteBuf, out: MutableList<Any>) {
        out.add(TextWebSocketFrame(stompFrame.retain()))
    }

    override fun decode(ctx: ChannelHandlerContext, webSocketFrame: WebSocketFrame, out: MutableList<Any>) {
        if (webSocketFrame is TextWebSocketFrame) {
            out.add(webSocketFrame.content().retain())
        } else {
            ctx.close()
        }
    }
}