
package com.shengshijie.httpserver;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.ReferenceCountUtil;


@ChannelHandler.Sharable

public class InterceptorHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg)   {
        if (isPassed((FullHttpRequest) msg)){
            context.fireChannelRead(msg);
            return;
        }
        ReferenceCountUtil.release(msg);
        context.writeAndFlush(HttpResponse.make(HttpResponseStatus.UNAUTHORIZED)).addListener(ChannelFutureListener.CLOSE);
    }

    private boolean isPassed(FullHttpRequest request){
        return true;
    }

}
