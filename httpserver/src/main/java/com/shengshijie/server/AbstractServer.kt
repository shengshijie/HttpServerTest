package com.shengshijie.server

import com.shengshijie.server.http.FilterLoggingHandler
import com.shengshijie.server.http.HttpHandler
import com.shengshijie.server.http.config.Config
import com.shengshijie.server.http.router.RouterManager
import com.shengshijie.server.http.scanner.IPackageScanner
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
import io.netty.handler.stream.ChunkedWriteHandler

abstract class AbstractServer : IServer {

    private var bossGroup: EventLoopGroup? = null
    private var workerGroup: EventLoopGroup? = null
    private var running: Boolean = false
    private var mConfig: Config = Config

    abstract fun getPackageScanner(): IPackageScanner

    override fun start(config: Config) {
        mConfig = config
        LogManager.setLogImpl(mConfig.log)
        if (running) {
            LogManager.i("server is already running")
            return
        }
        if (Config.debug) mConfig.packageNameList.add("com.shengshijie.server.http.controller")
        RouterManager.registerRouter(getPackageScanner(), mConfig.packageNameList)
        try {
            val bootstrap = ServerBootstrap()
            bossGroup = NioEventLoopGroup()
            workerGroup = NioEventLoopGroup()
            bootstrap.group(bossGroup, workerGroup)
            bootstrap.channel(NioServerSocketChannel::class.java)
            bootstrap.childOption(NioChannelOption.SO_BACKLOG, mConfig.backlog)
            bootstrap.childOption(NioChannelOption.TCP_NODELAY, mConfig.tcpNodelay)
            bootstrap.childOption(NioChannelOption.SO_REUSEADDR, mConfig.soReuseaddr)
            bootstrap.childOption(NioChannelOption.SO_KEEPALIVE, mConfig.soKeepalive)
            bootstrap.childOption(NioChannelOption.SO_RCVBUF, mConfig.soRcvbuf)
            bootstrap.childOption(NioChannelOption.SO_SNDBUF, mConfig.soSndbuf)
            bootstrap.childHandler(object : ChannelInitializer<SocketChannel>() {
                public override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast("decoder", HttpRequestDecoder())
                    ch.pipeline().addLast("encoder", HttpResponseEncoder())
                    ch.pipeline().addLast("aggregator", HttpObjectAggregator(mConfig.maxContentLength))
                    ch.pipeline().addLast("chunked", ChunkedWriteHandler())
                    ch.pipeline().addLast("logging", FilterLoggingHandler())
                    ch.pipeline().addLast("httpHandler", HttpHandler())
                }
            })
            val channelFuture = bootstrap.bind(mConfig.port).syncUninterruptibly().addListener { LogManager.i("server start at port ${mConfig.port}") }
            running = true
            channelFuture.channel().closeFuture().addListener {
                bossGroup?.shutdownGracefully()
                workerGroup?.shutdownGracefully()
            }
        } catch (e: Exception) {
            LogManager.i("server start error:" + e.message)
        }
    }

    override fun stop() {
        if (!running) {
            LogManager.i("server is already stop")
            return
        }
        try {
            bossGroup?.shutdownGracefully()
            workerGroup?.shutdownGracefully()
            bossGroup = null
            workerGroup = null
            running = false
            LogManager.i("server stop")
        } catch (e: Exception) {
            LogManager.i("server stop error:" + e.message)
        }
    }

}