package com.shengshijie.server.http.serialize

import java.lang.reflect.Type

internal class GsonSerialize : Serialize {

    override fun <T> serialize(t: T): String {
        return GsonInstance.gson.toJson(t)
    }

    override fun <T> deserialization(string: String, clazz: Type): T {
        return GsonInstance.gson.fromJson(string, clazz)
    }

}