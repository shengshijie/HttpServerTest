package com.shengshijie.server.http.router

import kotlin.reflect.KFunction

internal class Invoker ( val method: KFunction<*>, val instance: Any?, val args: List<Parameter>)