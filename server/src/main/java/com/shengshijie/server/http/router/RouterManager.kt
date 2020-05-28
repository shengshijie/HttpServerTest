package com.shengshijie.server.http.router

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.RequestBody
import com.shengshijie.server.http.annotation.RequestMapping
import com.shengshijie.server.http.annotation.RequestParam
import com.shengshijie.server.http.exception.RequestException
import com.shengshijie.server.http.filter.SignFilter
import com.shengshijie.server.http.request.IHttpRequest
import com.shengshijie.server.http.scanner.IPackageScanner
import java.util.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

internal class RouterManager {

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
                            val params = mutableListOf<Parameter>()
                            for (param in func.parameters) {
                                if (param.kind !== KParameter.Kind.INSTANCE) {
                                    params.add(Parameter(
                                            hasRequestBody = param.findAnnotation<RequestBody>() != null,
                                            name = param.findAnnotation<RequestParam>()?.value ?: param.name,
                                            type = param.type,
                                            required = param.findAnnotation<RequestParam>()?.required ?: true,
                                            defaultValue = param.findAnnotation<RequestParam>()?.defaultValue ?: ""))
                                }
                            }
                            if (params.any { it.hasRequestBody } && params.size > 1) {
                                throw RuntimeException("request body must have only one parameter:[${clazz.simpleName} ${func.name}(${params.map { it.name }.joinToString()})]")
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
            throw RequestException("path not found: [${requestPath}]")
        }
        if (!methodAllowed) {
            throw RequestException("method not allowed: [${requestMethod}]")
        }
        return functionHandlerMap[path]
                ?: throw RequestException("router not found: [${requestPath} ${requestMethod}]")
    }

}