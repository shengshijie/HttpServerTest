package com.shengshijie.servertest.util

import com.shengshijie.servertest.GsonInstance
import io.netty.buffer.ByteBufUtil
import java.security.MessageDigest
import java.util.*

fun getParamSign(any: Any): String {
    var map: MutableMap<String, String?> = mutableMapOf()
    map = GsonInstance.gson.fromJson(GsonInstance.gson.toJson(any), map.javaClass)
    val keys = map.keys.toMutableList()
    keys.sortBy { it }
    val sign = StringBuilder()
    for (key in keys) {
        if (map[key] != null && "${map[key]}" != "") {
            sign.append(key).append("=").append("${map[key]}").append("&")
        }
    }
    sign.append("59201CF6589202CB2CDAB26752472112")
    return ByteBufUtil.hexDump(
            MessageDigest.getInstance("MD5").digest(sign.toString().toByteArray())
    ).toUpperCase(Locale.getDefault())
}