package com.shengshijie.server.http.filter

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.IHttpRequest
import com.shengshijie.server.http.IHttpResponse
import com.shengshijie.server.http.exception.BusinessException
import com.shengshijie.server.http.utils.HttpRequestUtil
import io.netty.buffer.ByteBufUtil
import io.netty.util.CharsetUtil
import java.security.MessageDigest

class SignFilter : Filter {

    private val timeKey = "time"
    private val nonceKey = "nonce"
    private val signKey = "sign"
    private val expireTime = 5 * 60 * 1000L
    private val nonceList = mutableListOf<String>()

    override fun preFilter(request: IHttpRequest, response: IHttpResponse): Boolean {
        ServerManager.mLogManager.i("preFilter ${request.uri()} ${response.content().toString(CharsetUtil.UTF_8)}")
        val parameterMap = HttpRequestUtil.getParameterMap(request)
        val nonce = parameterMap[nonceKey]?.firstOrNull() ?: ""
        if (nonce.isBlank()) throw BusinessException("request nonce param")
        if (nonceList.contains(nonce)) throw BusinessException("duplicate request")
        nonceList.add(nonce)
        val start = parameterMap[timeKey]?.firstOrNull()?.toLong() ?: 0L
        val now = System.currentTimeMillis()
        if (now - start > expireTime || start > now) throw BusinessException("request expired")
        val sign = parameterMap[signKey]?.firstOrNull() ?: throw BusinessException("not signed")
        parameterMap.remove(signKey)
        val signLocal = getParamSign(parameterMap)
        if (sign != signLocal) throw BusinessException("incorrect sign")
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




