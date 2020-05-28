package com.shengshijie.server.http.serialize

import kotlin.reflect.KClass

interface Serialize {

    fun  serialize(any: Any?): String

    fun  deserialization(string: String, clazz: KClass<*>): Any?

}