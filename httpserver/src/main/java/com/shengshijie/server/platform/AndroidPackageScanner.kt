package com.shengshijie.server.platform

import android.content.Context
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.scanner.IPackageScanner
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import java.util.*
import kotlin.reflect.KClass

class AndroidPackageScanner(private val mContext: Context) : IPackageScanner {

    override fun scan(packageName: String): List<KClass<*>> {
        val classes: MutableList<KClass<out Any>> = ArrayList()
        try {
            val classLoader = Thread.currentThread().contextClassLoader as PathClassLoader
            val dex = DexFile(mContext.packageResourcePath)
            val entries = dex.entries()
            while (entries.hasMoreElements()) {
                val entryName = entries.nextElement()
                if (entryName.contains(packageName)||entryName.contains("com.shengshijie.server.http.controller") ) {
                    val entryClass = Class.forName(entryName, true, classLoader).kotlin
                    classes.add(entryClass)
                }
            }
        } catch (ignore: Exception) {
            //
        }
        return classes
    }

}