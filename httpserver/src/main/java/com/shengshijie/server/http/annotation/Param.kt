package com.shengshijie.server.http.annotation

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Param(val key: String = "", val defaultValue: String = "", val notNull: Boolean = false, val notBlank: Boolean = false)