package com.shengshijie.server.platform.java

import com.shengshijie.server.http.utils.ExceptionUtils
import com.shengshijie.server.log.LogManager
import java.io.File
import java.net.URISyntaxException
import java.net.URL
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarInputStream
import kotlin.collections.ArrayList
import kotlin.reflect.KClass

object JavaPackageScannerUtils {

    fun scan(packageName: String): List<KClass<*>> {
        val classes: MutableList<KClass<*>> = LinkedList()
        try {
            val loader = Thread.currentThread().contextClassLoader!!
            val urls = loader.getResources(packageName.replace('.', '/'))
            while (urls.hasMoreElements()) {
                val uri = urls.nextElement().toURI()
                when (uri.scheme.toLowerCase(Locale.getDefault())) {
                    "jar" -> scanFromJarProtocol(loader, classes, uri.rawSchemeSpecificPart)
                    "file" -> scanFromFileProtocol(loader, classes, File(uri).path, packageName)
                    else -> throw URISyntaxException(uri.scheme, "unknown schema " + uri.scheme)
                }
            }
        } catch (e: Exception) {
            LogManager.w(ExceptionUtils.toString(e))
        }
        return classes
    }

    private fun loadClass(loader: ClassLoader, classPath: String): KClass<*>? {
        return loader.loadClass(classPath.substring(0, classPath.length - 6)).kotlin
    }

    private fun scanFromFileProtocol(loader: ClassLoader, classes: MutableList<KClass<*>>, dir: String, packageName: String) {
        listAllClassNames(dir).forEach { className: String ->
            loadClass(loader, String.format("%s.%s", packageName, className))?.let { classes.add(it) }
        }
    }

    private fun scanFromJarProtocol(loader: ClassLoader, classes: MutableList<KClass<*>>, fullPath: String) {
        val jar = fullPath.substring(0, fullPath.lastIndexOf('!'))
        val parent = fullPath.substring(fullPath.lastIndexOf('!') + 2)
        var e: JarEntry?
        JarInputStream(URL(jar).openStream()).use {
            while (it.nextJarEntry.also { jar -> e = jar } != null) {
                var className = e!!.name
                if (!e!!.isDirectory && className.startsWith(parent) && className.endsWith(".class") && !className.contains("$")) {
                    className = className.replace('/', '.')
                    loadClass(loader, className)?.let { k -> classes.add(k) }
                }
                it.closeEntry()
            }
        }
    }

    private fun listAllClassNames(classpath: String): List<String> {
        val classNames: MutableList<String> = ArrayList()
        val allClassFiles = FileUtils.listFiles(File(classpath), ".class", true)
        val index = if (classpath.endsWith(File.separator)) classpath.length else classpath.length + 1
        for (classFile in allClassFiles) {
            val className = classFile.absolutePath.substring(index).replace(File.separatorChar, '.')
            classNames.add(className)
        }
        return classNames
    }
}