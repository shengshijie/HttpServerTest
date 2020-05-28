package com.shengshijie.server.http.serialize

import kotlin.reflect.KClass

internal class GsonSerialize : Serialize {

    override fun serialize(any: Any?): String {
        return GsonInstance.gson.toJson(any)
    }

    override fun deserialization(string: String, clazz: KClass<*>): Any? {
        return GsonInstance.gson.fromJson(string, clazz.java)
    }

}