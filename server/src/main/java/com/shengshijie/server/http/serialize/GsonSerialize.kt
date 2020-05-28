package com.shengshijie.server.http.serialize

import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

internal class GsonSerialize : Serialize {

    override fun <T> serialize(t: T): String {
        return GsonInstance.gson.toJson(t)
    }

    override fun <T> deserialization(string: String, clazz: KType): T? {
        return GsonInstance.gson.fromJson(string, clazz.javaType)
    }

}