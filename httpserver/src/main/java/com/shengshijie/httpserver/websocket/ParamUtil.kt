package com.shengshijie.httpserver.websocket

import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.QueryStringDecoder
import io.netty.handler.codec.http.multipart.*
import java.util.*

object ParamUtil {
    fun getRequestParams(request: HttpRequest?): Map<String, String> {
        val requestParams: MutableMap<String, String> = HashMap()
        if (request!!.method() === HttpMethod.GET) {
            val decoder = QueryStringDecoder(request!!.uri())
            val params = decoder.parameters()
            for ((key, value) in params) {
                requestParams[key] = value[0]
            }
        }
        if (request!!.method() === HttpMethod.POST) {
            val decoder = HttpPostRequestDecoder(DefaultHttpDataFactory(false), request)
            val postData = decoder.bodyHttpDatas
            for (data in postData) {
                if (data.httpDataType == InterfaceHttpData.HttpDataType.Attribute) {
                    val attribute = data as MemoryAttribute
                    requestParams[attribute.name] = attribute.value
                }
            }
        }
        return requestParams
    }

    fun getFileUpload(request: HttpRequest?): FileUpload? {
        if (request!!.method() === HttpMethod.POST) {
            val decoder = HttpPostRequestDecoder(DefaultHttpDataFactory(false), request)
            val postData = decoder.bodyHttpDatas
            for (data in postData) {
                if (data.httpDataType == InterfaceHttpData.HttpDataType.FileUpload) {
                    return data as FileUpload
                }
            }
        }
        return null
    }

    fun getUri(request: HttpRequest): String {
        return QueryStringDecoder(request.uri()).path()
    }
}