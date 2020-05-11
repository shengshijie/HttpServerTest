package com.shengshijie.server.platform.defaultplatform

import com.shengshijie.server.http.scanner.IPackageScanner
import java.util.*
import kotlin.reflect.KClass

class DefaultPackageScanner : IPackageScanner {

    override fun scan(packageNameList: List<String>): List<KClass<*>> {
        val classes: MutableList<KClass<out Any>> = ArrayList()
        packageNameList.forEach {
            classes.addAll(PackageScanner.scan(it))
        }
        return classes
    }

}