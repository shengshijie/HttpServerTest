package com.shengshijie.server.http.controller

import com.shengshijie.server.http.RawResponse
import com.shengshijie.server.http.RawResponse.Companion.ok
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.RequestMapping
import com.shengshijie.server.http.config.Config
import com.shengshijie.server.http.router.RouterManager
import io.netty.handler.codec.http.FullHttpRequest

@Controller
@RequestMapping(value = "/")
class StatusController {

    @RequestMapping(value = "status", method = "POST")
    fun status(): RawResponse<Any> {
        return ok(Status.getStatus())
    }

    @RequestMapping(value = "services", method = "POST")
    fun services(): RawResponse<Any> {
        return ok(RouterManager.getRouters())
    }

    @RequestMapping(value = "config", method = "POST")
    fun config(): RawResponse<Any> {
        return ok(Config.getConfig())
    }

}