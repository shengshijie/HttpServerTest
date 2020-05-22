package com.shengshijie.server.http.filter

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.IHttpRequest
import com.shengshijie.server.http.IHttpResponse
import com.shengshijie.server.http.exception.BusinessException
import com.shengshijie.server.http.utils.HttpRequestUtil
import io.netty.buffer.ByteBufUtil
import io.netty.util.CharsetUtil
import java.security.MessageDigest

internal class SignFilter : Filter {

    private val timeKey = "time"
    private val nonceKey = "nonce"
    private val signKey = "sign"
    private val expireTime = 5 * 60 * 1000L
    private val nonceList = mutableListOf<String>()

    override fun preFilter(request: IHttpRequest, response: IHttpResponse): Boolean {
        signAuthentication(request, response)
        return false
    }

    override fun postFilter(request: IHttpRequest, response: IHttpResponse) {
        ServerManager.mLogManager.i("postFilter ${request.uri()} ${response.content().toString(CharsetUtil.UTF_8)}")
    }

    private fun signAuthentication(request: IHttpRequest, response: IHttpResponse) {
        ServerManager.mLogManager.i("preFilter ${request.uri()} ${response.content().toString(CharsetUtil.UTF_8)}")
        val paramMap = request.getParamMap()
        val nonce = paramMap[nonceKey] ?: ""
        if (nonce.isBlank()) throw BusinessException("request param [$nonceKey]")
        if (nonceList.contains(nonce)) throw BusinessException("duplicate request")
        nonceList.add(nonce)
        val start = paramMap[timeKey]?.toLong() ?: throw BusinessException("request param [$timeKey]")
        val now = System.currentTimeMillis()
        if (now - start > expireTime || start > now) throw BusinessException("request expired")
        val sign = paramMap[signKey] ?: throw BusinessException("request param [$signKey]")
        paramMap.remove(signKey)
        if (sign != getParamSign(paramMap)) throw BusinessException("incorrect sign")
    }

    private fun getParamSign(map: MutableMap<String, String?>): String? {
        val keys = map.keys.toMutableList()
        keys.sortBy { it }
        val sign = StringBuilder()
        for (key in keys) {
            if (map[key] != null) {
                sign.append(key).append("=").append(map[key]).append("&")
            }
        }
        sign.deleteCharAt(sign.lastIndex)
        sign.append(ServerManager.mServerConfig.salt)
        return ByteBufUtil.hexDump(MessageDigest.getInstance("SHA-1").digest(sign.toString().toByteArray()))
    }

}




