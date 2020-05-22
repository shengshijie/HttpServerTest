package com.shengshijie.server.http.response

import com.shengshijie.server.ServerManager

internal data class WrappedResponse<T>(private val code: Int, private val message: String, private val data: T) {

    companion object {

        @JvmStatic
        fun <T> ok(data: T,msg: String): WrappedResponse<T> {
            return WrappedResponse(ServerManager.mServerConfig.successCode, msg, data)
        }

        @JvmStatic
        fun fail(code: Int, msg: String): WrappedResponse<Any> {
            return WrappedResponse(code, msg, Unit)
        }
    }

}