package com.shengshijie.server.http.serialize

import java.lang.reflect.Type


interface Serialize {

    fun <T> serialize(t: T): String

    fun <T> deserialization(string: String, clazz: Type): T

}