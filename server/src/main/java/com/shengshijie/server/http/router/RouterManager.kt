package com.shengshijie.server.http.router

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.IHttpRequest
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.Param
import com.shengshijie.server.http.annotation.RequestMapping
import com.shengshijie.server.http.exception.ServerException
import com.shengshijie.server.http.filter.SignFilter
import com.shengshijie.server.http.scanner.IPackageScanner
import java.util.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

class RouterManager {

    private val functionHandlerMap = HashMap<Path, Router>()

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
                                params.add(Parameter(param.findAnnotation<Param>()?.value
                                        ?: param.name, param.type))
                            }
                            val router = Router(Invoker(func, clazz.createInstance(), params))
                            if (ServerManager.mServerConfig.sign) router.addFilters(arrayListOf(SignFilter()))
                            functionHandlerMap[path] = router
                        }
                    }
                }
            }
        }
    }

    fun matchRouter(fullRequest: IHttpRequest): Router {
        val requestPath = fullRequest.uri().split("?")[0]
        val requestMethod = fullRequest.method().name()
        var path: Path? = null
        var methodAllowed = false
        for ((p) in functionHandlerMap) {
            if (p.uri == requestPath) {
                path = p
                if (requestMethod.equals(path.method, ignoreCase = true)) {
                    methodAllowed = true
                }
            }
        }
        if (path == null) {
            throw ServerException("path not found: [${requestPath}]")
        }
        if (!methodAllowed) {
            throw ServerException("method not allowed: [${requestMethod}]")
        }
        return functionHandlerMap[path]
                ?: throw ServerException("router not found: [${requestPath} ${requestMethod}]")
    }

}