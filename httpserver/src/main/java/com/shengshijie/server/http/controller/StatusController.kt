package com.shengshijie.server.http.controller

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.RawResponse
import com.shengshijie.server.http.RawResponse.Companion.ok
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.RequestMapping

@Controller
@RequestMapping(value = "/")
class StatusController {

    @RequestMapping(value = "status", method = "POST")
    fun status(): RawResponse<Any> {
        return ok(ServerManager.mStatus.getStatus())
    }

    @RequestMapping(value = "services", method = "POST")
    fun services(): RawResponse<Any> {
        return ok(ServerManager.mRouterManager.getRouters())
    }

    @RequestMapping(value = "config", method = "POST")
    fun config(): RawResponse<Any> {
        return ok(ServerManager.mServerConfig.getConfig())
    }

}