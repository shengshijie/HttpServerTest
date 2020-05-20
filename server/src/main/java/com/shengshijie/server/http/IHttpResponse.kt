package com.shengshijie.server.http

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion

interface IHttpResponse {
    fun status(): HttpResponseStatus
    fun setStatus(status: HttpResponseStatus): HttpResponse
    fun setProtocolVersion(version: HttpVersion): HttpResponse
    fun content(): ByteBuf
    fun headers(): HttpHeaders
}