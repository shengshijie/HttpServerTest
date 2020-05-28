package com.shengshijie.server.http.annotation

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequestParam(val value: String = "", val required: Boolean = true, val defaultValue: String = "")