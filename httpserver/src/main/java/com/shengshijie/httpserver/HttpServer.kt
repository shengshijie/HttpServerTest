package com.shengshijie.httpserver

import android.util.Log
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
import io.netty.handler.stream.ChunkedWriteHandler


object HttpServer {

    @JvmStatic
    @JvmOverloads
    fun start(port: Int, log: (level:Int,content: String) -> Unit = {_,_->}) {
        val bootstrap = ServerBootstrap()
        val bossGroup: EventLoopGroup = NioEventLoopGroup()
        val workerGroup: EventLoopGroup = NioEventLoopGroup()
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
        val channelFuture = bootstrap.bind(port).syncUninterruptibly().addListener { Log.i("SERVER", "server start at port $port") }
        channelFuture.channel().closeFuture().addListener {
            Log.i("SERVER", "server shutdown")
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}