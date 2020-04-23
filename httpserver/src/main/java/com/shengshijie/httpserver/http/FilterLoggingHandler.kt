package com.shengshijie.httpserver.http

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import java.net.SocketAddress

class FilterLoggingHandler(var log: (level:Int,content: String) -> Unit) : LoggingHandler(LogLevel.TRACE) {

    override fun channelRegistered(ctx: ChannelHandlerContext) {
        log(0,"channelRegistered")
        ctx.fireChannelRegistered()
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        log(0,"channelUnregistered")
        ctx.fireChannelUnregistered()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log(0,"channelActive")
        ctx.fireChannelActive()
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log(0,"channelInactive")
        ctx.fireChannelInactive()
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        log(0,"userEventTriggered")
        ctx.fireUserEventTriggered(evt)
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        log(1,"""<<SEND>> ${ctx.channel()}  
$msg""")
        ctx.write(msg, promise)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val request = msg as HttpRequest
        val log = """${request.method()} ${request.uri()} ${request.protocolVersion()}
${HttpHeaderNames.CONTENT_TYPE}: ${request.headers()[HttpHeaderNames.CONTENT_TYPE]}
${HttpHeaderNames.CONTENT_LENGTH}: ${request.headers()[HttpHeaderNames.CONTENT_LENGTH]}
"""
        log(1,"""<<RECV>> ${ctx.channel()}  
$log""")

        ctx.fireChannelRead(msg)
    }

    override fun bind(ctx: ChannelHandlerContext, localAddress: SocketAddress, promise: ChannelPromise) {
        log(0,"bind localAddress: $localAddress")
        ctx.bind(localAddress, promise)
    }

    override fun connect(ctx: ChannelHandlerContext, remoteAddress: SocketAddress, localAddress: SocketAddress, promise: ChannelPromise) {
        log(0,"bind remoteAddress: $remoteAddress + localAddress: $localAddress ")
        ctx.connect(remoteAddress, localAddress, promise)
    }

    override fun disconnect(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        log(0,"disconnect")
        ctx.disconnect(promise)
    }

    override fun close(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        log(0,"close")
        ctx.close(promise)
    }

    override fun deregister(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        log(0,"deregister")
        ctx.deregister(promise)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        log(0,"channelReadComplete")
        ctx.fireChannelReadComplete()
    }

    override fun channelWritabilityChanged(ctx: ChannelHandlerContext) {
        log(0,"channelWritabilityChanged")
        ctx.fireChannelWritabilityChanged()
    }

    override fun flush(ctx: ChannelHandlerContext) {
        log(0,"flush")
        ctx.flush()
    }
}