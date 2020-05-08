package com.shengshijie.server.http

import io.netty.buffer.ByteBuf
import io.netty.buffer.PooledByteBufAllocator
import io.netty.handler.codec.http.*

class HttpResponse private constructor(status: HttpResponseStatus, buffer: ByteBuf) : DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buffer) {

    private var content: String? = null

    override fun toString(): String {

        val builder = StringBuilder()
        builder.append(protocolVersion().toString()).append(" ").append(status().toString()).append("\n")
        builder.append(HttpHeaderNames.CONTENT_TYPE).append(": ").append(headers()[HttpHeaderNames.CONTENT_TYPE]).append("\n")
        builder.append(HttpHeaderNames.CONTENT_LENGTH).append(": ").append(headers()[HttpHeaderNames.CONTENT_LENGTH]).append("\n")
        builder.append("content-body").append(": ").append(content).append("\n")
        return builder.toString()
    }

    companion object {

        private val BYTE_BUF_ALLOCATOR = PooledByteBufAllocator(false)
        private const val CONTENT_NORMAL_200 = "{\"code\":200,\"message\":\"OK\"}"
        private const val CONTENT_ERROR_401 = "{\"code\":401,\"message\":\"UNAUTHORIZED\"}"
        private const val CONTENT_ERROR_404 = "{\"code\":404,\"message\":\"REQUEST PATH NOT FOUND\"}"
        private const val CONTENT_ERROR_405 = "{\"code\":405,\"message\":\"METHOD NOT ALLOWED\"}"
        private const val CONTENT_ERROR_500 = "{\"code\":500,\"message\":\"%s\"}"

        fun make(status: HttpResponseStatus): FullHttpResponse {
            if (HttpResponseStatus.UNAUTHORIZED === status) {
                return make(HttpResponseStatus.UNAUTHORIZED, CONTENT_ERROR_401)
            }
            if (HttpResponseStatus.NOT_FOUND === status) {
                return make(HttpResponseStatus.NOT_FOUND, CONTENT_ERROR_404)
            }
            return if (HttpResponseStatus.METHOD_NOT_ALLOWED === status) {
                make(HttpResponseStatus.METHOD_NOT_ALLOWED, CONTENT_ERROR_405)
            } else make(HttpResponseStatus.OK, CONTENT_NORMAL_200)
        }

        fun makeError(exception: Exception): FullHttpResponse {
            val message = exception.javaClass.name + ":" + exception.message
            return make(HttpResponseStatus.INTERNAL_SERVER_ERROR, String.format(CONTENT_ERROR_500, message))
        }

        fun ok(content: String): FullHttpResponse {
            return make(HttpResponseStatus.OK, content)
        }

        private fun make(status: HttpResponseStatus, content: String): FullHttpResponse {
            val body = content.toByteArray()
            val buffer = BYTE_BUF_ALLOCATOR.buffer(body.size)
            buffer.writeBytes(body)
            val response = HttpResponse(status, buffer)
            response.content = content
            return response
        }
    }

    init {
        headers()[HttpHeaderNames.CONTENT_TYPE] = "application/json"
        headers().setInt(HttpHeaderNames.CONTENT_LENGTH, content().readableBytes())
        headers()[HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN] = "*"
        headers()[HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS] = "Origin, X-Requested-With, Content-Type, Accept, RCS-ACCESS-TOKEN"
        headers()[HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS] = "GET,POST,PUT,DELETE"
    }
}