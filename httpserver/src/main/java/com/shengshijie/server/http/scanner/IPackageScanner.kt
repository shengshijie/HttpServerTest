package com.shengshijie.server.http.scanner

import kotlin.reflect.KClass

interface IPackageScanner {

    fun scan(packageName: String): List<KClass<*>>

}