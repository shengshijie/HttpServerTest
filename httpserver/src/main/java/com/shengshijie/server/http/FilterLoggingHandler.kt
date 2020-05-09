package com.shengshijie.server.http

import com.shengshijie.server.LogManager
import com.shengshijie.server.http.config.Config
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import java.net.SocketAddress

class FilterLoggingHandler : LoggingHandler(Config.logLevel) {

    override fun channelRegistered(ctx: ChannelHandlerContext) {
        LogManager.d("channelRegistered")
        ctx.fireChannelRegistered()
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        LogManager.d("channelUnregistered")
        ctx.fireChannelUnregistered()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        LogManager.d("channelActive")
        ctx.fireChannelActive()
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        LogManager.d("channelInactive")
        ctx.fireChannelInactive()
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        LogManager.d("userEventTriggered")
        ctx.fireUserEventTriggered(evt)
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        LogManager.i("""<<RESPONSE>> ${ctx.channel()}  
$msg""")
        ctx.write(msg, promise)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val request = msg as HttpRequest
        val log = """${request.method()} ${request.uri()} ${request.protocolVersion()}
${HttpHeaderNames.CONTENT_TYPE}: ${request.headers()[HttpHeaderNames.CONTENT_TYPE]}
${HttpHeaderNames.CONTENT_LENGTH}: ${request.headers()[HttpHeaderNames.CONTENT_LENGTH]}
"""
        LogManager.i("""<<REQUEST>> ${ctx.channel()}  
$log""")

        ctx.fireChannelRead(msg)
    }

    override fun bind(ctx: ChannelHandlerContext, localAddress: SocketAddress, promise: ChannelPromise) {
        LogManager.d("bind localAddress: $localAddress")
        ctx.bind(localAddress, promise)
    }

    override fun connect(ctx: ChannelHandlerContext, remoteAddress: SocketAddress, localAddress: SocketAddress, promise: ChannelPromise) {
        LogManager.d("bind remoteAddress: $remoteAddress + localAddress: $localAddress ")
        ctx.connect(remoteAddress, localAddress, promise)
    }

    override fun disconnect(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        LogManager.d("disconnect")
        ctx.disconnect(promise)
    }

    override fun close(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        LogManager.d("close")
        ctx.close(promise)
    }

    override fun deregister(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        LogManager.d("deregister")
        ctx.deregister(promise)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        LogManager.d("channelReadComplete")
        ctx.fireChannelReadComplete()
    }

    override fun channelWritabilityChanged(ctx: ChannelHandlerContext) {
        LogManager.d("channelWritabilityChanged")
        ctx.fireChannelWritabilityChanged()
    }

    override fun flush(ctx: ChannelHandlerContext) {
        LogManager.d("flush")
        ctx.flush()
    }
}