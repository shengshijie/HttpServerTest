package com.shengshijie.server.http.router

import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.Param
import com.shengshijie.server.http.annotation.RequestMapping
import com.shengshijie.server.http.exception.MethodNotAllowedException
import com.shengshijie.server.http.exception.PathNotFoundException
import com.shengshijie.server.http.scanner.IPackageScanner
import io.netty.handler.codec.http.FullHttpRequest
import java.util.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

object RouterManager {

    private val functionHandlerMap = HashMap<Path, Invoker>()

    fun getRouters(): List<Path> {
        val routerList = mutableListOf<Path>()
        functionHandlerMap.forEach {
            routerList.add(it.key)
        }
        return routerList
    }

    fun registerRouter(packageScanner: IPackageScanner, packageNameList: List<String>) {
        packageScanner.scan(packageNameList).forEach { clazz ->
            val controllerAnnotation = clazz.findAnnotation<Controller>()
            val requestMappingAnnotation = clazz.findAnnotation<RequestMapping>()
            if (controllerAnnotation != null && requestMappingAnnotation != null) {
                clazz.declaredMemberFunctions.forEach { func ->
                    func.findAnnotation<RequestMapping>()?.apply {
                        val path: Path? = Path.make(this, requestMappingAnnotation)
                        if (path != null && !functionHandlerMap.containsKey(path)) {
                            val params = arrayListOf<Parameter>()
                            func.parameters.forEach { param ->
                                val paramAnnotation = param.findAnnotation<Param>()
                                if (paramAnnotation != null) {
                                    params.add(Parameter(paramAnnotation.value, param.type))
                                } else {
                                    params.add(Parameter(param.name, param.type))
                                }
                            }
                            functionHandlerMap[path] = Invoker(func, clazz.createInstance(), params)
                        }
                    }
                }
            }
        }
    }

    @Throws(PathNotFoundException::class, MethodNotAllowedException::class)
    fun matchRouter(fullRequest: FullHttpRequest): Invoker? {
        var requestPath: Path? = null
        var methodAllowed = false
        for ((path) in functionHandlerMap) {
            if (path.uri == fullRequest.uri().split("?")[0]) {
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