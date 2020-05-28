package com.shengshijie.server.http.utils

import com.shengshijie.server.http.request.IHttpRequest
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.QueryStringDecoder
import io.netty.util.CharsetUtil
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

internal object HttpRequestUtil {

    fun getParameterMap(request: IHttpRequest): MutableMap<String, MutableList<String>?> {
        var paramMap: MutableMap<String, MutableList<String>?> = mutableMapOf()
        when (request.method()) {
            HttpMethod.GET -> {
                paramMap = QueryStringDecoder(request.uri(), CharsetUtil.UTF_8).parameters()
            }
            HttpMethod.POST -> {
                paramMap = getPostParamMap(request)
            }
        }
        return paramMap
    }

    private fun getPostParamMap(request: IHttpRequest): MutableMap<String, MutableList<String>?> {
        var paramMap: MutableMap<String, MutableList<String>?> = HashMap()
        val contentTypes = request.headers()[HttpHeaderNames.CONTENT_TYPE]
                ?.split(";")
                ?.toTypedArray()
                ?: return paramMap
        val uft8Content = request.uft8Content()
        when (true) {
            contentTypes.contains(HttpHeaderValues.APPLICATION_JSON.toString()) -> {
                when (JSONTokener(uft8Content).nextValue()) {
                    is JSONObject -> {
                        val jsonObject = JSONObject(uft8Content)
                        jsonObject.keys().forEach {
                            val value = jsonObject.get(it)
                            val valueList: MutableList<String> = paramMap[it] ?: mutableListOf()
                            if (PrimitiveTypeUtil.isPriType(value)) {
                                valueList.add(value.toString())
                                paramMap[it] = valueList
                            }
                        }
                    }
                    is JSONArray -> {
                        throw RuntimeException("jsonArray not support,please use request body")
                    }
                    else -> {
                        throw RuntimeException("unsupported format")
                    }
                }
            }
            contentTypes.contains(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString()) -> {
                paramMap = QueryStringDecoder(uft8Content, false).parameters()
            }
        }
        return paramMap
    }

}