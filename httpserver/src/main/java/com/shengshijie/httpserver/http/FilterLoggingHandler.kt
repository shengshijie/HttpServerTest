package com.shengshijie.httpserver.http

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import java.net.SocketAddress

class FilterLoggingHandler(var log: (level: LogLevel, content: String) -> Unit) : LoggingHandler(LogLevel.TRACE) {

    override fun channelRegistered(ctx: ChannelHandlerContext) {
        log(LogLevel.DEBUG, "channelRegistered")
        ctx.fireChannelRegistered()
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        log(LogLevel.DEBUG, "channelUnregistered")
        ctx.fireChannelUnregistered()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log(LogLevel.DEBUG, "channelActive")
        ctx.fireChannelActive()
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log(LogLevel.DEBUG, "channelInactive")
        ctx.fireChannelInactive()
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        log(LogLevel.DEBUG, "userEventTriggered")
        ctx.fireUserEventTriggered(evt)
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        log(LogLevel.INFO, """<<SEND>> ${ctx.channel()}  
$msg""")
        ctx.write(msg, promise)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val request = msg as HttpRequest
        val log = """${request.method()} ${request.uri()} ${request.protocolVersion()}
${HttpHeaderNames.CONTENT_TYPE}: ${request.headers()[HttpHeaderNames.CONTENT_TYPE]}
${HttpHeaderNames.CONTENT_LENGTH}: ${request.headers()[HttpHeaderNames.CONTENT_LENGTH]}
"""
        log(LogLevel.INFO, """<<RECV>> ${ctx.channel()}  
$log""")

        ctx.fireChannelRead(msg)
    }

    override fun bind(ctx: ChannelHandlerContext, localAddress: SocketAddress, promise: ChannelPromise) {
        log(LogLevel.DEBUG, "bind localAddress: $localAddress")
        ctx.bind(localAddress, promise)
    }

    override fun connect(ctx: ChannelHandlerContext, remoteAddress: SocketAddress, localAddress: SocketAddress, promise: ChannelPromise) {
        log(LogLevel.DEBUG, "bind remoteAddress: $remoteAddress + localAddress: $localAddress ")
        ctx.connect(remoteAddress, localAddress, promise)
    }

    override fun disconnect(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        log(LogLevel.DEBUG, "disconnect")
        ctx.disconnect(promise)
    }

    override fun close(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        log(LogLevel.DEBUG, "close")
        ctx.close(promise)
    }

    override fun deregister(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        log(LogLevel.DEBUG, "deregister")
        ctx.deregister(promise)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        log(LogLevel.DEBUG, "channelReadComplete")
        ctx.fireChannelReadComplete()
    }

    override fun channelWritabilityChanged(ctx: ChannelHandlerContext) {
        log(LogLevel.DEBUG, "channelWritabilityChanged")
        ctx.fireChannelWritabilityChanged()
    }

    override fun flush(ctx: ChannelHandlerContext) {
        log(LogLevel.DEBUG, "flush")
        ctx.flush()
    }
}