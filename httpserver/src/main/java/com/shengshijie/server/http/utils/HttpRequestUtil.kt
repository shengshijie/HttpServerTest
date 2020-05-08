package com.shengshijie.server.http.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.shengshijie.server.http.ContentType
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import java.lang.reflect.Array
import java.util.*
import kotlin.reflect.KClass

object HttpRequestUtil {

    fun getParameterMap(request: HttpRequest): MutableMap<String, MutableList<String>?> {
        var paramMap: MutableMap<String, MutableList<String>?> = mutableMapOf()
        val method = request.method()
        if (HttpMethod.GET == method) {
            val uri = request.uri()
            val queryDecoder = QueryStringDecoder(uri, CharsetUtil.UTF_8)
            paramMap = queryDecoder.parameters()
        } else if (HttpMethod.POST == method) {
            val fullRequest = request as FullHttpRequest
            paramMap = getPostParamMap(fullRequest)
        }
        return paramMap
    }

    private fun getPostParamMap(fullRequest: FullHttpRequest): MutableMap<String, MutableList<String>?> {
        var paramMap: MutableMap<String, MutableList<String>?> = HashMap()
        when (fullRequest.headers()[HttpHeaderNames.CONTENT_TYPE].split(";").toTypedArray()[0]) {
            ContentType.APPLICATION_JSON.toString() -> {
                for ((key, value) in JSON.parseObject(fullRequest.content().toString(CharsetUtil.UTF_8))) {
                    val valueType: KClass<*> = value.javaClass.kotlin
                    var valueList: MutableList<String>?
                    valueList = if (paramMap.containsKey(key)) {
                        paramMap[key]
                    } else {
                        ArrayList()
                    }
                    when {
                        PrimitiveTypeUtil.isPriType(valueType) -> {
                            valueList?.add(value.toString())
                            paramMap[key] = valueList
                        }
                        PrimitiveTypeUtil.isPriArrayType(valueType) -> {
                            val length = Array.getLength(value)
                            for (i in 0 until length) {
                                val arrayItem = Array.get(value, i).toString()
                                valueList?.add(arrayItem)
                            }
                            paramMap[key] = valueList
                        }
                        valueType is MutableList<*> -> {
                            if (valueType == JSONArray::class.java) {
                                val jArray = JSONArray.parseArray(value.toString())
                                for (i in jArray.indices) {
                                    valueList?.add(jArray.getString(i))
                                }
                            } else {
                                valueList = value as ArrayList<String>
                            }
                            paramMap[key] = valueList
                        }
                        valueType is MutableMap<*, *> -> {
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
            ContentType.APPLICATION_FORM_URLENCODED.toString() -> {
                val jsonStr = fullRequest.content().toString(CharsetUtil.UTF_8)
                val queryDecoder = QueryStringDecoder(jsonStr, false)
                paramMap = queryDecoder.parameters()
            }
        }
        return paramMap
    }

}