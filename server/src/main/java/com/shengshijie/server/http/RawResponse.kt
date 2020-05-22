package com.shengshijie.server.http

import com.shengshijie.server.ServerManager

internal data class RawResponse<T>(private val code: Int, private val message: String, private val data: T) {

    companion object {

        @JvmStatic
        fun <T> ok(data: T,msg: String): RawResponse<T> {
            return RawResponse(ServerManager.mServerConfig.successCode, msg, data)
        }

        @JvmStatic
        fun fail(code: Int, msg: String): RawResponse<Any> {
            return RawResponse(code, msg, Unit)
        }
    }

}