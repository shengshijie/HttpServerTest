package com.shengshijie.server.http.router

import kotlin.reflect.KType

internal class Parameter(val hasRequestBody: Boolean = false, val name: String?, val type: KType, val required: Boolean, val defaultValue: String)