package com.shengshijie.server.http.scanner

interface IPackageScanner {

    fun scan(packageName: String): List<Class<*>>

}