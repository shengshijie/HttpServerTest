package com.shengshijie.server.websocket.server

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.DefaultFileRegion
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile

@Sharable
class StompWebSocketClientPageHandler private constructor() : SimpleChannelInboundHandler<FullHttpRequest>() {
    override fun channelRead0(ctx: ChannelHandlerContext, request: FullHttpRequest) {
        if (request.headers().contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true)) {
            ctx.fireChannelRead(request.retain())
            return
        }
        if (request.decoderResult().isFailure) {
            val badRequest: FullHttpResponse = DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.BAD_REQUEST)
            sendResponse(badRequest, ctx, true)
            return
        }
        if (!sendResource(request, ctx)) {
            val notFound: FullHttpResponse = DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.NOT_FOUND)
            notFound.headers()[HttpHeaderNames.CONTENT_TYPE] = HttpHeaderValues.TEXT_PLAIN
            val payload = "Requested resource " + request.uri() + " not found"
            notFound.content().writeCharSequence(payload, CharsetUtil.UTF_8)
            HttpUtil.setContentLength(notFound, notFound.content().readableBytes().toLong())
            sendResponse(notFound, ctx, true)
        }
    }

    companion object {
        val INSTANCE = StompWebSocketClientPageHandler()
        private fun sendResource(request: FullHttpRequest, ctx: ChannelHandlerContext): Boolean {
            if (request.uri().isEmpty() || !request.uri().startsWith("/")) {
                return false
            }
            var requestResource = request.uri().substring(1)
            if (requestResource.isEmpty()) {
                requestResource = "index.html"
            }
            val resourceUrl = INSTANCE.javaClass.getResource(requestResource) ?: return false
            var raf: RandomAccessFile? = null
            var fileLength = -1L
            try {
                raf = RandomAccessFile(resourceUrl.file, "r")
                fileLength = raf.length()
            } catch (fne: FileNotFoundException) {
                println("File not found " + fne.message)
                return false
            } catch (io: IOException) {
                println("Cannot read file length " + io.message)
                return false
            } finally {
                if (fileLength < 0 && raf != null) {
                    try {
                        raf.close()
                    } catch (io: IOException) {
                        // Nothing to do
                    }
                }
            }
            val response: HttpResponse = DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK)
            HttpUtil.setContentLength(response, fileLength)
            var contentType = "application/octet-stream"
            if (requestResource.endsWith("html")) {
                contentType = "text/html; charset=UTF-8"
            } else if (requestResource.endsWith("css")) {
                contentType = "text/css; charset=UTF-8"
            } else if (requestResource.endsWith("js")) {
                contentType = "application/javascript"
            }
            response.headers()[HttpHeaderNames.CONTENT_TYPE] = contentType
            sendResponse(response, ctx, false)
            ctx.write(DefaultFileRegion(raf!!.channel, 0, fileLength))
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
            return true
        }

        private fun sendResponse(response: HttpResponse, ctx: ChannelHandlerContext, autoFlush: Boolean) {
            if (HttpUtil.isKeepAlive(response)) {
                if (response.protocolVersion() == HttpVersion.HTTP_1_0) {
                    response.headers()[HttpHeaderNames.CONNECTION] = HttpHeaderValues.KEEP_ALIVE
                }
                ctx.write(response)
            } else {
                response.headers()[HttpHeaderNames.CONNECTION] = HttpHeaderValues.CLOSE
                ctx.write(response).addListener(ChannelFutureListener.CLOSE)
            }
            if (autoFlush) {
                ctx.flush()
            }
        }
    }
}