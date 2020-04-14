package com.shengshijie.httpserver

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.util.ReferenceCountUtil

@Sharable
class InterceptorHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(context: ChannelHandlerContext, msg: Any) {
        if (isPassed(msg as FullHttpRequest)) {
            context.fireChannelRead(msg)
            return
        }
        ReferenceCountUtil.release(msg)
        context.writeAndFlush(HttpResponse.make(HttpResponseStatus.UNAUTHORIZED)).addListener(ChannelFutureListener.CLOSE)
    }

    private fun isPassed(request: FullHttpRequest): Boolean {
        return true
    }
}