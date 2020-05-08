package com.shengshijie.server

import com.shengshijie.server.http.FilterLoggingHandler
import com.shengshijie.server.http.HttpHandler
import com.shengshijie.server.http.router.RouterManager
import com.shengshijie.server.http.scanner.IPackageScanner
import com.shengshijie.server.platform.AndroidPackageScanner
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

abstract class AbstractServer : IServer {

    private var bossGroup: EventLoopGroup? = null
    private var workerGroup: EventLoopGroup? = null
    private var running: Boolean = false

    abstract fun getPackageScanner(): IPackageScanner

    abstract fun getPackageName():String

    @ExperimentalStdlibApi
    override fun start(port: Int) {
        if (running) {
            LogManager.i( "server is already running")
            return
        }
        RouterManager.registerRouter(getPackageScanner(),getPackageName())
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
                    ch.pipeline().addLast("logging", FilterLoggingHandler())
                    ch.pipeline().addLast("httpHandler", HttpHandler())
                }
            })
            val channelFuture = bootstrap.bind(port).syncUninterruptibly().addListener {   LogManager.i( "server start at port $port") }
            running = true
            channelFuture.channel().closeFuture().addListener {
                bossGroup?.shutdownGracefully()
                workerGroup?.shutdownGracefully()
            }
        } catch (e: Exception) {
            LogManager.i( "server start error:" + e.message)
        }
    }

    override fun stop() {
        if (!running) {
            LogManager.i( "server is already stop")
            return
        }
        try {
            bossGroup?.shutdownGracefully()
            workerGroup?.shutdownGracefully()
            bossGroup = null
            workerGroup = null
            running = false
            LogManager.i( "server stop")
        } catch (e: Exception) {
            LogManager.i( "server stop error:" + e.message)
        }
    }

}