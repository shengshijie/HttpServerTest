package com.shengshijie.server.http.scanner

import kotlin.reflect.KClass

interface IPackageScanner {

    fun scan(packageNameList: List<String>): List<KClass<*>>

}