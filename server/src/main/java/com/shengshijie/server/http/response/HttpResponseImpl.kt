package com.shengshijie.server.http.response

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil

internal class HttpResponseImpl(val fullHttpResponse: FullHttpResponse) : IHttpResponse {

    override fun status(): HttpResponseStatus{
        return fullHttpResponse.status()
    }

    override fun setStatus(status: HttpResponseStatus): HttpResponse {
        return fullHttpResponse.setStatus(status)
    }

    override fun setProtocolVersion(version: HttpVersion): HttpResponse {
        return fullHttpResponse.setProtocolVersion(version)
    }

    override fun content(): ByteBuf {
        return fullHttpResponse.content()
    }

    override fun headers(): HttpHeaders {
        return fullHttpResponse.headers()
    }

    override fun uft8Content(): String {
        return fullHttpResponse.content().toString(CharsetUtil.UTF_8)
    }

}