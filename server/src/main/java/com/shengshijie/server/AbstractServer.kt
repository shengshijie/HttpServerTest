package com.shengshijie.server

import com.shengshijie.server.http.FilterLoggingHandler
import com.shengshijie.server.http.HttpHandler
import com.shengshijie.server.http.utils.ExceptionUtils
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

    override fun start(result: (Result<String>) -> Unit) {
        try {
            val bootstrap = ServerBootstrap()
            bossGroup = NioEventLoopGroup()
            workerGroup = NioEventLoopGroup()
            bootstrap.group(bossGroup, workerGroup)
            bootstrap.channel(NioServerSocketChannel::class.java)
            bootstrap.childOption(NioChannelOption.SO_BACKLOG, ServerManager.mServerConfig.backlog)
            bootstrap.childOption(NioChannelOption.TCP_NODELAY, ServerManager.mServerConfig.tcpNodelay)
            bootstrap.childOption(NioChannelOption.SO_REUSEADDR, ServerManager.mServerConfig.soReuseaddr)
            bootstrap.childOption(NioChannelOption.SO_KEEPALIVE, ServerManager.mServerConfig.soKeepalive)
            bootstrap.childOption(NioChannelOption.SO_RCVBUF, ServerManager.mServerConfig.soRcvbuf)
            bootstrap.childOption(NioChannelOption.SO_SNDBUF, ServerManager.mServerConfig.soSndbuf)
            bootstrap.childHandler(object : ChannelInitializer<SocketChannel>() {
                public override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast("decoder", HttpRequestDecoder())
                    ch.pipeline().addLast("encoder", HttpResponseEncoder())
                    ch.pipeline().addLast("aggregator", HttpObjectAggregator(ServerManager.mServerConfig.maxContentLength))
                    ch.pipeline().addLast("chunked", ChunkedWriteHandler())
                    ch.pipeline().addLast("logging", FilterLoggingHandler())
                    ch.pipeline().addLast("httpHandler", HttpHandler())
                }
            })
            val channelFuture = bootstrap.bind(ServerManager.mServerConfig.port).syncUninterruptibly().addListener {
                result(Result.success("server start at port ${ServerManager.mServerConfig.port}"))
            }
            channelFuture.channel().closeFuture().addListener {
                bossGroup?.shutdownGracefully()
                workerGroup?.shutdownGracefully()
            }
        } catch (e: Exception) {
            result(Result.error("server start error:" + ExceptionUtils.toString(e)))
        }
    }

    override fun stop(result: (Result<String>) -> Unit) {
        try {
            bossGroup?.shutdownGracefully()
            workerGroup?.shutdownGracefully()
            bossGroup = null
            workerGroup = null
            result(Result.success("server stop success"))
        } catch (e: Exception) {
            result(Result.error("server stop error:" + ExceptionUtils.toString(e)))
        }
    }

}