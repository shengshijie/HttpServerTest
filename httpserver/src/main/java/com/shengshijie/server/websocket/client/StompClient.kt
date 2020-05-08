package com.shengshijie.server.websocket.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.stomp.StompSubframeAggregator
import io.netty.handler.codec.stomp.StompSubframeDecoder
import io.netty.handler.codec.stomp.StompSubframeEncoder

object StompClient {
    val SSL = System.getProperty("ssl") != null
    val HOST = System.getProperty("host", "127.0.0.1")
    val PORT = System.getProperty("port", "61613")!!.toInt()
    val LOGIN = System.getProperty("login", "guest")
    val PASSCODE = System.getProperty("passcode", "guest")
    val TOPIC = System.getProperty("topic", "jms.topic.exampleTopic")

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val group: EventLoopGroup = NioEventLoopGroup()
        try {
            val b = Bootstrap()
            b.group(group).channel(NioSocketChannel::class.java)
            b.handler(object : ChannelInitializer<SocketChannel>() {
                @Throws(Exception::class)
                override fun initChannel(ch: SocketChannel) {
                    val pipeline = ch.pipeline()
                    pipeline.addLast("decoder", StompSubframeDecoder())
                    pipeline.addLast("encoder", StompSubframeEncoder())
                    pipeline.addLast("aggregator", StompSubframeAggregator(1048576))
                    pipeline.addLast("handler", StompClientHandler())
                }
            })
            b.connect(HOST, PORT).sync().channel().closeFuture().sync()
        } finally {
            group.shutdownGracefully()
        }
    }
}