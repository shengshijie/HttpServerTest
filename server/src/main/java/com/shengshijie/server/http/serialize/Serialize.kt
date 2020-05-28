package com.shengshijie.server.http.serialize

import kotlin.reflect.KType

interface Serialize {

    fun <T> serialize(t: T): String

    fun <T> deserialization(string: String, clazz: KType): T?

}