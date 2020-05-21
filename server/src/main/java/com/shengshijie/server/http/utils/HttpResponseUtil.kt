package com.shengshijie.server.http.utils

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.IHttpResponse
import com.shengshijie.server.http.RawResponse
import io.netty.buffer.ByteBufUtil

object HttpResponseUtil {

    fun writeOK(response: IHttpResponse, any: Any) {
        response.content().clear()
        ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(RawResponse.ok(any)))
    }

    fun writeFail(response: IHttpResponse, msg: String) {
        response.content().clear()
        ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(RawResponse.fail(msg)))
    }

}