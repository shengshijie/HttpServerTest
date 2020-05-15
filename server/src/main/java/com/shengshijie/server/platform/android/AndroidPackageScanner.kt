package com.shengshijie.server.platform.android

import android.content.Context
import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.scanner.IPackageScanner
import com.shengshijie.server.http.utils.ExceptionUtils
import com.shengshijie.server.log.LogManager
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import java.util.*
import kotlin.reflect.KClass

class AndroidPackageScanner(private val mContext: Context) : IPackageScanner {

    override fun scan(packageNameList: List<String>): List<KClass<*>> {
        val classes: MutableList<KClass<out Any>> = ArrayList()
        try {
            val classLoader = Thread.currentThread().contextClassLoader as PathClassLoader
            val entries = DexFile(mContext.packageResourcePath).entries()
            while (entries.hasMoreElements()) {
                val entryName = entries.nextElement()
                packageNameList.forEach {
                    if (entryName.contains(it)) {
                        val entryClass = Class.forName(entryName, true, classLoader).kotlin
                        classes.add(entryClass)
                    }
                }
            }
        } catch (e: Exception) {
            ServerManager.mLogManager.w("scan error: ${ExceptionUtils.toString(e)}")
        }
        return classes
    }

}