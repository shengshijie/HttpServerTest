package com.shengshijie.server.http.utils

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.IHttpResponse
import com.shengshijie.server.http.RawResponse
import io.netty.buffer.ByteBufUtil
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpResponseStatus

object HttpResponseUtil {

    fun writeOKResponse(response: IHttpResponse, any: Any? = "success") {
        response.headers()[HttpHeaderNames.CONTENT_TYPE] = "application/json"
        response.content().clear()
        ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(RawResponse.ok(any ?: Unit)))
    }

    fun writeFailResponse(response: IHttpResponse, msg: String = "error") {
        response.headers()[HttpHeaderNames.CONTENT_TYPE] = "application/json"
        response.content().clear()
        ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(RawResponse.fail(msg)))
    }

    fun writeFail(response: IHttpResponse, status: HttpResponseStatus, msg: String) {
        response.setStatus(status)
        ByteBufUtil.writeUtf8(response.content(), msg)
    }

}