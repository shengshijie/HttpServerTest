package com.shengshijie.server.http.utils

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.response.IHttpResponse
import com.shengshijie.server.http.response.WrappedResponse
import io.netty.buffer.ByteBufUtil
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpResponseStatus

internal object HttpResponseUtil {

    fun writeOKResponse(response: IHttpResponse, any: Any? = Unit, msg: String = "success") {
        response.headers()[HttpHeaderNames.CONTENT_TYPE] = "application/json"
        response.content().clear()
        ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(WrappedResponse.ok(any ?: Unit, msg)))
    }

    fun writeFailResponse(response: IHttpResponse, code: Int = ServerManager.mServerConfig.errorCode, msg: String = "error") {
        response.headers()[HttpHeaderNames.CONTENT_TYPE] = "application/json"
        response.content().clear()
        ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(WrappedResponse.fail(code, msg)))
    }

    fun writeRawResponse(response: IHttpResponse, data: Any?) {
        response.headers()[HttpHeaderNames.CONTENT_TYPE] = "application/json"
        response.content().clear()
        ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(data))
    }

    fun writeFail(response: IHttpResponse, status: HttpResponseStatus, msg: String) {
        response.setStatus(status)
        ByteBufUtil.writeUtf8(response.content(), msg)
    }

}