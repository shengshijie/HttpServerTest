package com.shengshijie.httpserver

import com.shengshijie.httpserver.file.FileUploadHandler
import com.shengshijie.httpserver.http.FilterLoggingHandler
import com.shengshijie.httpserver.http.HttpHandler
import com.shengshijie.httpserver.http.InterceptorHandler
import com.shengshijie.httpserver.websocket.WebSocketHandler
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioChannelOption
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.logging.LogLevel
import io.netty.handler.stream.ChunkedWriteHandler


object HttpServer {

    private var bossGroup: EventLoopGroup? = null
    private var workerGroup: EventLoopGroup? = null
    private var running: Boolean = false

    @JvmStatic
    @JvmOverloads
    fun start(port: Int, log: (level: LogLevel, content: String) -> Unit = { _, _ -> }) {
        if (running) {
            log(LogLevel.INFO, "server is already running")
            return
        }
        try {
            val bootstrap = ServerBootstrap()
            bossGroup = NioEventLoopGroup()
            workerGroup = NioEventLoopGroup()
            bootstrap.group(bossGroup, workerGroup)
            bootstrap.channel(NioServerSocketChannel::class.java)
            bootstrap.childOption(NioChannelOption.TCP_NODELAY, true)
            bootstrap.childOption(NioChannelOption.SO_REUSEADDR, true)
            bootstrap.childOption(NioChannelOption.SO_KEEPALIVE, false)
            bootstrap.childOption(NioChannelOption.SO_RCVBUF, 2048)
            bootstrap.childOption(NioChannelOption.SO_SNDBUF, 2048)
            bootstrap.childHandler(object : ChannelInitializer<SocketChannel>() {
                public override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast("decoder", HttpRequestDecoder())
                    ch.pipeline().addLast("encoder", HttpResponseEncoder())
                    ch.pipeline().addLast("aggregator", HttpObjectAggregator(1024 * 1024 * 5))
                    ch.pipeline().addLast("chunked", ChunkedWriteHandler())
                    ch.pipeline().addLast("logging", FilterLoggingHandler(log))
                    ch.pipeline().addLast("interceptor", InterceptorHandler())
                    ch.pipeline().addLast("dispatchHandler", DispatchHandler())
                    ch.pipeline().addLast("httpHandler", HttpHandler().apply { registerRouter() })
                    ch.pipeline().addLast("webSocketHandler", WebSocketHandler())
                    ch.pipeline().addLast("fileUploadHandler", FileUploadHandler())
                }
            })
            val channelFuture = bootstrap.bind(port).syncUninterruptibly().addListener { log(LogLevel.INFO, "server start at port $port") }
            running = true
            channelFuture.channel().closeFuture().addListener {
                bossGroup?.shutdownGracefully()
                workerGroup?.shutdownGracefully()
            }
        } catch (e: Exception) {
            log(LogLevel.INFO, "server start error:" + e.message)
        }
    }

    @JvmStatic
    fun stop(log: (level: LogLevel, content: String) -> Unit = { _, _ -> }) {
        if (!running) {
            log(LogLevel.INFO, "server is already stop")
            return
        }
        try {
            bossGroup?.shutdownGracefully()
            workerGroup?.shutdownGracefully()
            bossGroup = null
            workerGroup = null
            running = false
            log(LogLevel.INFO, "server stop")
        } catch (e: Exception) {
            log(LogLevel.INFO, "server stop error:" + e.message)
        }
    }

}