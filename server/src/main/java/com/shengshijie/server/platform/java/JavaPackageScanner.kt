package com.shengshijie.server.platform.java

import com.shengshijie.server.http.scanner.IPackageScanner
import java.util.*
import kotlin.reflect.KClass

class JavaPackageScanner : IPackageScanner {

    override fun scan(packageNameList: List<String>): List<KClass<*>> {
        val classes: MutableList<KClass<out Any>> = ArrayList()
        packageNameList.forEach {
            classes.addAll(JavaPackageScannerUtils.scan(it))
        }
        return classes
    }

}