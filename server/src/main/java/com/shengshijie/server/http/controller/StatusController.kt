package com.shengshijie.server.http.controller

import com.shengshijie.server.ServerManager
import com.shengshijie.server.http.RawResponse
import com.shengshijie.server.http.annotation.Controller
import com.shengshijie.server.http.annotation.RequestMapping

@Controller
@RequestMapping(value = "/")
class StatusController {

    @RequestMapping(value = "status", method = "POST")
    fun status(): Any {
        return ServerManager.mStatus.getStatus()
    }

    @RequestMapping(value = "services", method = "POST")
    fun services(): Any {
        return ServerManager.mRouterManager.getRouters()
    }

    @RequestMapping(value = "config", method = "POST")
    fun config(): Any {
        return ServerManager.mServerConfig.getConfig()
    }

}