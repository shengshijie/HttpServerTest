package com.shengshijie.server.http.router

import com.shengshijie.server.LogManager
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.RequestMapping
import com.shengshijie.server.http.exception.MethodNotAllowedException
import com.shengshijie.server.http.exception.PathNotFoundException
import com.shengshijie.server.http.scanner.IPackageScanner
import io.netty.handler.codec.http.FullHttpRequest
import java.lang.reflect.Method
import java.util.*

object RouterManager {

    private val functionHandlerMap = HashMap<Path, Invoker>()

    fun getRouters(): List<Path> {
        val routerList = mutableListOf<Path>()
        functionHandlerMap.forEach {
            routerList.add(it.key)
        }
        return routerList
    }

    fun registerRouter(packageScanner: IPackageScanner, packageName: String) {
        packageScanner.scan(packageName).forEach { clazz ->
            if (clazz.isAnnotationPresent(Controller::class.java) && clazz.isAnnotationPresent(RequestMapping::class.java)) {
                val rootRequestMappingAnnotation = clazz.getAnnotation(RequestMapping::class.java)!!
                val methods: Array<Method> = clazz.declaredMethods
                methods.forEach {
                    if (it.isAnnotationPresent(RequestMapping::class.java)) {
                        val requestMappingAnnotation = it.getAnnotation(RequestMapping::class.java)!!
                        val path: Path? = Path.make(requestMappingAnnotation, rootRequestMappingAnnotation)
                        if (path != null && !functionHandlerMap.containsKey(path)) {
                            functionHandlerMap[path] = Invoker(it, clazz.newInstance())
                        }
                    }
                }
            }
        }
        if (functionHandlerMap.isNotEmpty()) {
            functionHandlerMap.forEach {
                LogManager.i(it.key.uri + " -- " + it.key.method)
            }
        } else {
            LogManager.i("routers not found")
        }
    }

    @Throws(PathNotFoundException::class, MethodNotAllowedException::class)
    fun matchRouter(fullRequest: FullHttpRequest): Invoker? {
        var requestPath: Path? = null
        var methodAllowed = false
        for ((path) in functionHandlerMap) {
            if (path.uri == fullRequest.uri()) {
                requestPath = path
                if (fullRequest.method.name().equals(path.method, ignoreCase = true)) {
                    methodAllowed = true
                }
            }
        }
        if (requestPath == null) {
            throw PathNotFoundException()
        }
        if (!methodAllowed) {
            throw MethodNotAllowedException()
        }
        return functionHandlerMap[requestPath]
    }

}