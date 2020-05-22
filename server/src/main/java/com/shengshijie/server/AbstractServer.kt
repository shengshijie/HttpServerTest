package com.shengshijie.server

import com.shengshijie.server.http.utils.ExceptionUtils
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioChannelOption
import io.netty.channel.socket.nio.NioServerSocketChannel

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
            bootstrap.childHandler(Initializer())
            val channelFuture = bootstrap
                    .bind(ServerManager.mServerConfig.port)
                    .syncUninterruptibly()
                    .addListener {
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