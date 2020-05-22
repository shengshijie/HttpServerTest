package com.shengshijie.server.http.request

import com.shengshijie.server.http.utils.HttpRequestUtil
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.*

internal class HttpRequestImpl(val fullHttpRequest: FullHttpRequest) : IHttpRequest {

    private val mParams by lazy {
        val params = mutableMapOf<String, String?>()
        HttpRequestUtil.getParameterMap(this).forEach {
            params[it.key] = it.value?.firstOrNull()
        }
        params
    }

    override fun method(): HttpMethod {
        return fullHttpRequest.method()
    }

    override fun setMethod(method: HttpMethod): HttpRequest {
        return fullHttpRequest.setMethod(method)
    }

    override fun uri(): String {
        return fullHttpRequest.uri()
    }

    override fun setUri(uri: String): HttpRequest {
        return fullHttpRequest.setUri(uri)
    }

    override fun setProtocolVersion(version: HttpVersion?): HttpRequest {
        return fullHttpRequest.setProtocolVersion(version)
    }

    override fun content(): ByteBuf {
        return fullHttpRequest.content()
    }

    override fun headers(): HttpHeaders {
        return fullHttpRequest.headers()
    }

    override fun protocolVersion(): HttpVersion {
        return fullHttpRequest.protocolVersion()
    }

    override fun getParamMap(): MutableMap<String, String?> {
        return mParams
    }

}