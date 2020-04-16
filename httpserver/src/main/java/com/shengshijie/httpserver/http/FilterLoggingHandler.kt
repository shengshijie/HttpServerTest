package com.shengshijie.httpserver.http

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import java.net.SocketAddress

class FilterLoggingHandler : LoggingHandler(LogLevel.INFO) {

    override fun channelRegistered(ctx: ChannelHandlerContext) {
        ctx.fireChannelRegistered()
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        ctx.fireChannelUnregistered()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.fireChannelActive()
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        ctx.fireChannelInactive()
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        ctx.fireUserEventTriggered(evt)
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        if (logger.isEnabled(internalLevel)) {
            logger.log(internalLevel, """${ctx.channel()} WRITE 
$msg""")
        }
        ctx.write(msg, promise)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (logger.isEnabled(internalLevel)) {
            val request = msg as HttpRequest
            val log = """${request.method()} ${request.uri()} ${request.protocolVersion()}
${HttpHeaderNames.CONTENT_TYPE}: ${request.headers()[HttpHeaderNames.CONTENT_TYPE]}
${HttpHeaderNames.CONTENT_LENGTH}: ${request.headers()[HttpHeaderNames.CONTENT_LENGTH]}
"""
            logger.log(internalLevel, """${ctx.channel()} READ 
$log""")
        }
        ctx.fireChannelRead(msg)
    }

    override fun bind(ctx: ChannelHandlerContext, localAddress: SocketAddress, promise: ChannelPromise) {
        ctx.bind(localAddress, promise)
    }

    override fun connect(ctx: ChannelHandlerContext, remoteAddress: SocketAddress, localAddress: SocketAddress, promise: ChannelPromise) {
        ctx.connect(remoteAddress, localAddress, promise)
    }

    override fun disconnect(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        ctx.disconnect(promise)
    }

    override fun close(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        ctx.close(promise)
    }

    override fun deregister(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        ctx.deregister(promise)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.fireChannelReadComplete()
    }

    override fun channelWritabilityChanged(ctx: ChannelHandlerContext) {
        ctx.fireChannelWritabilityChanged()
    }

    override fun flush(ctx: ChannelHandlerContext) {
        ctx.flush()
    }
}