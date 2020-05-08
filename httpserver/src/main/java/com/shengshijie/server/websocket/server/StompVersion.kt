package com.shengshijie.server.websocket.server

import io.netty.util.AttributeKey
import io.netty.util.internal.StringUtil
import kotlin.collections.ArrayList

enum class StompVersion(private val version: String, private val subProtocol: String) {

    STOMP_V11("1.1", "v11.stomp"), STOMP_V12("1.2", "v12.stomp");

    companion object {
        val CHANNEL_ATTRIBUTE_KEY = AttributeKey.valueOf<StompVersion>("stomp_version")
        var SUB_PROTOCOLS: String? = null
        fun findBySubProtocol(subProtocol: String?): StompVersion {
            if (subProtocol != null) {
                for (stompVersion in values()) {
                    if (stompVersion.subProtocol() == subProtocol) {
                        return stompVersion
                    }
                }
            }
            throw IllegalArgumentException("Not found StompVersion for '$subProtocol'")
        }

        init {
            val subProtocols: List<String> = ArrayList(values().size)
            for (stompVersion in values()) {
                subProtocols.plus(stompVersion.subProtocol)
            }
            SUB_PROTOCOLS = StringUtil.join(",", subProtocols).toString()
        }
    }

    fun version(): String {
        return version
    }

    fun subProtocol(): String {
        return subProtocol
    }

}