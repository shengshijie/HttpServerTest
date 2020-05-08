package com.shengshijie.server.http.annotation

@Target(AnnotationTarget.FUNCTION,AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequestMapping(val value: String = "", val method: String = "GET")