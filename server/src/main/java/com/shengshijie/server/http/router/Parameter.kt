package com.shengshijie.server.http.router

import kotlin.reflect.KClass

internal class Parameter(val hasRequestBody: Boolean = false, val name: String?, val clazz: KClass<*>, val required: Boolean, val defaultValue: String)