package com.shengshijie.server.http.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.shengshijie.server.http.IHttpRequest
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import java.lang.reflect.Array
import java.util.*

object HttpRequestUtil {

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

    private fun getPostParamMap(fullRequest: IHttpRequest): MutableMap<String, MutableList<String>?> {
        var paramMap: MutableMap<String, MutableList<String>?> = HashMap()
        val contentType = fullRequest.headers()[HttpHeaderNames.CONTENT_TYPE] ?: return paramMap
        when (contentType.split(";").toTypedArray()[0]) {
            HttpHeaderValues.APPLICATION_JSON.toString() -> {
                for ((key, value) in JSON.parseObject(fullRequest.content().toString(CharsetUtil.UTF_8))) {
                    var valueList: MutableList<String>? = if (paramMap.containsKey(key)) paramMap[key] else ArrayList()
                    when {
                        PrimitiveTypeUtil.isPriType(value) -> {
                            valueList?.add(value.toString())
                            paramMap[key] = valueList
                        }
                        PrimitiveTypeUtil.isPriArrayType(value) -> {
                            val length = Array.getLength(value)
                            for (i in 0 until length) {
                                valueList?.add(Array.get(value, i).toString())
                            }
                            paramMap[key] = valueList
                        }
                        value is MutableList<*> -> {
                            if (value == JSONArray::class) {
                                val jArray = JSONArray.parseArray(value.toString())
                                for (i in jArray.indices) {
                                    valueList?.add(jArray.getString(i))
                                }
                            } else {
                                valueList = value as ArrayList<String>
                            }
                            paramMap[key] = valueList
                        }
                        value is MutableMap<*, *> -> {
                            val tempMap = value as Map<String, String>
                            for ((key1, value1) in tempMap) {
                                val tempList: MutableList<String> = ArrayList()
                                tempList.add(value1)
                                paramMap[key1] = tempList
                            }
                        }
                    }
                }
            }
            HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString() -> {
                paramMap = QueryStringDecoder(fullRequest.content().toString(CharsetUtil.UTF_8), false).parameters()
            }
        }
        return paramMap
    }

}