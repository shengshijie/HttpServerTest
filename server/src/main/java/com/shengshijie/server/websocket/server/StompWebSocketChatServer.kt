package com.shengshijie.server.websocket.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

class StompWebSocketChatServer {
    @Throws(Exception::class)
    fun start(port: Int) {
        val boosGroup = NioEventLoopGroup(1)
        val workerGroup = NioEventLoopGroup()
        try {
            val bootstrap = ServerBootstrap()
                    .group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(StompWebSocketChatServerInitializer("/chat"))
            bootstrap.bind(port).addListener { future ->
                if (future.isSuccess) {
                    println("Open your web browser and navigate to http://127.0.0.1:$PORT/")
                } else {
                    println("Cannot start server, follows exception " + future.cause())
                }
            }.channel().closeFuture().sync()
        } finally {
            boosGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }

    companion object {
        val PORT = System.getProperty("port", "8080")?.toInt()?:8080

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            StompWebSocketChatServer().start(PORT)
        }
    }
}