package com.shengshijie.server

import com.shengshijie.server.http.FilterLoggingHandler
import com.shengshijie.server.http.HttpHandler
import com.shengshijie.server.http.utils.HttpsUtils
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.codec.http.cors.CorsConfigBuilder
import io.netty.handler.codec.http.cors.CorsHandler
import io.netty.handler.stream.ChunkedWriteHandler

class Initializer : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()
        if (ServerManager.mServerConfig.enableSSL) {
            pipeline.addLast("ssl", HttpsUtils.getSSlHandler(ch))
        }
        pipeline.addLast("decoder", HttpRequestDecoder())
        pipeline.addLast("encoder", HttpResponseEncoder())
        pipeline.addLast("aggregator", HttpObjectAggregator(ServerManager.mServerConfig.maxContentLength))
        pipeline.addLast("chunked", ChunkedWriteHandler())
        if (ServerManager.mServerConfig.enableCors) {
            val corsHandler = CorsHandler(CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build())
            pipeline.addLast("ssl", corsHandler)
        }
        pipeline.addLast("logging", FilterLoggingHandler())
        pipeline.addLast("httpHandler", HttpHandler())
    }

}