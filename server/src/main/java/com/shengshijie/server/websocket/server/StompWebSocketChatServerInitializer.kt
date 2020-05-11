package com.shengshijie.server.websocket.server

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.util.internal.ObjectUtil

class StompWebSocketChatServerInitializer(chatPath: String) : ChannelInitializer<SocketChannel>() {
    private val chatPath: String
    private val stompWebSocketProtocolCodec: StompWebSocketProtocolCodec

    @Throws(Exception::class)
    override fun initChannel(channel: SocketChannel) {
        channel.pipeline()
                .addLast(HttpServerCodec())
                .addLast(HttpObjectAggregator(65536))
                .addLast(StompWebSocketClientPageHandler.Companion.INSTANCE)
                .addLast(WebSocketServerProtocolHandler(chatPath, StompVersion.Companion.SUB_PROTOCOLS))
                .addLast(stompWebSocketProtocolCodec)
    }

    init {
        this.chatPath = ObjectUtil.checkNotNull(chatPath, "chatPath")
        stompWebSocketProtocolCodec = StompWebSocketProtocolCodec()
    }
}