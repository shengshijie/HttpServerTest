package com.shengshijie.server.http

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.*

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

}