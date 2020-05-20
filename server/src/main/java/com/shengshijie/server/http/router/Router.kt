package com.shengshijie.server.http.router

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.filter.Filter
import com.shengshijie.server.http.IHttpRequest
import com.shengshijie.server.http.IHttpResponse
import com.shengshijie.server.http.exception.ServerException
import com.shengshijie.server.http.utils.HttpRequestUtil
import io.netty.buffer.ByteBufUtil
import io.netty.handler.codec.http.HttpHeaderNames

class Router(var invoker: Invoker) {

    private val filters = mutableListOf<Filter>()

    fun addFilters(filters: List<Filter>) {
        this.filters.addAll(filters)
    }

    fun call(request: IHttpRequest, response: IHttpResponse) {
        try {
            for (filter in filters) {
                if (filter.preFilter(request, response)) {
                    return
                }
            }
            val allArgs = arrayListOf<Any>()
            val missingArgs = arrayListOf<String?>()
            for (i in 1 until invoker.args.size) {
                HttpRequestUtil.getParameterMap(request)[invoker.args[i].name]?.apply { allArgs.add(this[0]) }
                        ?: missingArgs.add(invoker.args[i].name)
            }
            if (missingArgs.isNotEmpty()) {
                throw ServerException("missing parameter: [${missingArgs.joinToString()}]")
            }
            val obj = invoker.method.call(invoker.instance, *(allArgs.toArray()))
            response.headers()[HttpHeaderNames.CONTENT_TYPE] = "application/json"
            ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(obj))
        } catch (ex: Exception) {
            throw ServerException("request error :${ex.message}")
        } finally {
            for (filter in filters) {
                filter.postFilter(request, response)
            }
        }
    }

}