package com.shengshijie.server.http.filter

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.IHttpRequest
import com.shengshijie.server.http.IHttpResponse
import com.shengshijie.server.http.RawResponse
import com.shengshijie.server.http.utils.HttpRequestUtil
import io.netty.buffer.ByteBufUtil
import io.netty.util.CharsetUtil
import java.security.MessageDigest
import java.util.*

class SignFilter : Filter {

    override fun preFilter(request: IHttpRequest, response: IHttpResponse): Boolean {
        ServerManager.mLogManager.i("preFilter ${request.uri()} ${response.content().toString(CharsetUtil.UTF_8)}")
        val parameterMap = HttpRequestUtil.getParameterMap(request)
        val sign = parameterMap["sign"]?.firstOrNull()
        if (sign == null) {
            response.content().clear()
            ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(RawResponse.fail("not signed")))
            return true
        }
        val signLocal = getSign(parameterMap)
        if (sign != signLocal) {
            response.content().clear()
            ByteBufUtil.writeUtf8(response.content(), ServerManager.mSerialize.serialize(RawResponse.fail("incorrect sign")))
            return true
        }
        return false
    }

    override fun postFilter(request: IHttpRequest, response: IHttpResponse) {
        ServerManager.mLogManager.i("postFilter ${request.uri()} ${response.content().toString(CharsetUtil.UTF_8)}")
        super.postFilter(request, response)
    }

}

private fun <TK, TV> createSortedMap(map: Map<TK, TV>): Map<TK, TV> {
    val mapTemp = TreeMap<TK, TV> { str1, str2 -> str1.toString().compareTo(str2.toString()) }
    mapTemp.putAll(map)
    return mapTemp
}

private fun <TK, TV> toQueryString(map: Map<TK, TV>?, isSortedByKey: Boolean, excludeKeys: Array<String>): String {
    return if (null != map && map.isNotEmpty()) {
        val sb = StringBuilder()
        val mapTemp = if (isSortedByKey) createSortedMap(map) else map
        for (key in mapTemp.keys) {
            if (!(excludeKeys.isNotEmpty() && excludeKeys.contains(key.toString()))) {
                if (sb.isNotEmpty()) {
                    sb.append("&")
                }
                sb.append(key.toString()).append("=").append((map[key] ?: error("")).toString())
            }
        }
        sb.toString()
    } else {
        ""
    }
}

private fun sha1(input: String): String {
    val instance = MessageDigest.getInstance("SHA-1")
    val digest = instance.digest(input.toByteArray())
    return ByteBufUtil.hexDump(digest)
}

private fun getParamsMap(map: MutableMap<String, MutableList<String>?>): Map<String, Any?> {
    val params = mutableMapOf<String, Any?>()
    map.forEach {
        params[it.key] = it.value?.firstOrNull()
    }
    return params
}

fun getSign(map: MutableMap<String, MutableList<String>?>) = sha1(toQueryString(getParamsMap(map), true, arrayOf("sign")) + ServerManager.mServerConfig.salt)
