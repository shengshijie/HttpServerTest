package com.shengshijie.server.http.router

import com.shengshijie.server.http.IHttpRequest
import com.shengshijie.server.http.IHttpResponse
import com.shengshijie.server.http.exception.BusinessException
import com.shengshijie.server.http.exception.RequestException
import com.shengshijie.server.http.exception.ServerException
import com.shengshijie.server.http.filter.Filter
import com.shengshijie.server.http.utils.ExceptionUtils
import com.shengshijie.server.http.utils.HttpRequestUtil
import com.shengshijie.server.http.utils.HttpResponseUtil
import java.lang.reflect.InvocationTargetException

class Router(var invoker: Invoker) {

    private val filters = mutableListOf<Filter>()

    fun addFilters(filters: List<Filter>) {
        this.filters.addAll(filters)
    }

    fun call(request: IHttpRequest, response: IHttpResponse) {
        val allArgs = arrayListOf<Any>()
        val missingArgs = arrayListOf<String?>()
        try {
            for (filter in filters) {
                if (filter.preFilter(request, response)) {
                    return
                }
            }
            for (i in 1 until invoker.args.size) {
                HttpRequestUtil.getParameterMap(request)[invoker.args[i].name]?.apply { allArgs.add(this[0]) }
                        ?: missingArgs.add(invoker.args[i].name)
            }
            if (missingArgs.isNotEmpty()) {
                throw RequestException("missing parameter: [${missingArgs.joinToString()}]")
            }
            val obj = invoker.method.call(invoker.instance, *(allArgs.toArray()))
            HttpResponseUtil.writeOKResponse(response, obj)
        } catch (exception: Exception) {
            when (exception) {
                is BusinessException -> {
                    throw exception
                }
                is RequestException -> {
                    throw exception
                }
                is InvocationTargetException -> {
                    throw BusinessException(exception.targetException.message?:"")
                }
                else -> {
                    throw ServerException("server error: [${ExceptionUtils.toString(exception)}]")
                }
            }
        } finally {
            for (filter in filters) {
                filter.postFilter(request, response)
            }
        }
    }

}