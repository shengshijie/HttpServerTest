package com.shengshijie.httpserver

@Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequestMapping(val path: String = "", val method: String = "GET", val equal: Boolean = true) 