package com.shengshijie.server.http.filter

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.IHttpRequest
import com.shengshijie.server.http.IHttpResponse
import com.shengshijie.server.http.utils.HttpRequestUtil
import com.shengshijie.server.http.utils.HttpResponseUtil
import io.netty.buffer.ByteBufUtil
import io.netty.util.CharsetUtil
import java.security.MessageDigest

class SignFilter : Filter {

    private val timeStampKey = "timestamp"
    private val nonceKey = "nonce"
    private val signKey = "sign"
    private val expireTime = 5 * 60 * 1000L
    private val nonceList = mutableListOf<String>()

    override fun preFilter(request: IHttpRequest, response: IHttpResponse): Boolean {
        ServerManager.mLogManager.i("preFilter ${request.uri()} ${response.content().toString(CharsetUtil.UTF_8)}")
        val parameterMap = HttpRequestUtil.getParameterMap(request)
        val nonce = parameterMap[nonceKey]?.firstOrNull() ?: ""
        if (nonce.isBlank()) {
            HttpResponseUtil.writeFail(response, "request nonce param")
            return true
        }
        if (nonceList.contains(nonce)) {
            HttpResponseUtil.writeFail(response, "duplicate request")
            return true
        }
        nonceList.add(nonce)
        val start = parameterMap[timeStampKey]?.firstOrNull()?.toLong() ?: 0L
        val now = System.currentTimeMillis()
        if (now - start > expireTime || start > now) {
            HttpResponseUtil.writeFail(response, "request expired")
            return true
        }
        val sign = parameterMap[signKey]?.firstOrNull()
        if (sign == null) {
            HttpResponseUtil.writeFail(response, "not signed")
            return true
        }
        parameterMap.remove(signKey)
        val signLocal = getParamSign(parameterMap)
        if (sign != signLocal) {
            HttpResponseUtil.writeFail(response, "incorrect sign")
            return true
        }
        return false
    }

    override fun postFilter(request: IHttpRequest, response: IHttpResponse) {
        ServerManager.mLogManager.i("postFilter ${request.uri()} ${response.content().toString(CharsetUtil.UTF_8)}")
        super.postFilter(request, response)
    }

    private fun getParamSign(map: MutableMap<String, MutableList<String>?>): String? {
        val params = mutableMapOf<String, Any?>()
        map.forEach {
            params[it.key] = it.value?.firstOrNull()
        }
        val keys = params.keys.toMutableList()
        keys.sortBy { it }
        val sign = StringBuilder()
        for (key in keys) {
            if (params[key] != null) {
                sign.append(key).append("=").append(params[key]).append("&")
            }
        }
        sign.deleteCharAt(sign.lastIndex)
        sign.append(ServerManager.mServerConfig.salt)
        return ByteBufUtil.hexDump(MessageDigest.getInstance("SHA-1").digest(sign.toString().toByteArray()))
    }

}




