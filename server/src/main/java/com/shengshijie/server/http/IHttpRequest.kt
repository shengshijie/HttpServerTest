package com.shengshijie.server.http

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpVersion

interface IHttpRequest {
    fun method(): HttpMethod
    fun setMethod(method: HttpMethod): HttpRequest
    fun uri(): String
    fun setUri(uri: String): HttpRequest
    fun setProtocolVersion(version: HttpVersion?): HttpRequest
    fun content(): ByteBuf
    fun headers(): HttpHeaders
    fun protocolVersion(): HttpVersion
}