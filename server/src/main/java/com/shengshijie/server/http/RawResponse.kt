package com.shengshijie.server.http

data class RawResponse<T>(private val code: Int, private val message: String, private val data: T) {

    companion object {

        @JvmStatic
        fun <T> ok(data: T): RawResponse<T> {
            return RawResponse(200, "ok", data)
        }

        @JvmStatic
        fun ok(): RawResponse<Any> {
            return RawResponse(200, "ok", Unit)
        }

        @JvmStatic
        fun <T> ok(msg: String, data: T): RawResponse<T> {
            return RawResponse(200, msg, data)
        }

        @JvmStatic
        fun fail(msg: String): RawResponse<Any> {
            return RawResponse(500, msg, Unit)
        }

    }

}