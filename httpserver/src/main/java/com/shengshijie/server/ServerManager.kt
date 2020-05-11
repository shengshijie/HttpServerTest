package com.shengshijie.server

import com.shengshijie.server.http.config.ServerConfig
import com.shengshijie.server.http.controller.Status
import com.shengshijie.server.http.router.RouterManager

object ServerManager {

    private var mServer: IServer? = null
    private var running: Boolean = false
    internal var mServerConfig: ServerConfig = ServerConfig.defaultServerConfig
    internal lateinit var mRouterManager: RouterManager
    internal lateinit var mStatus: Status

    @JvmStatic
    @JvmOverloads
    fun start(serverConfig: ServerConfig, result: (Result<String>) -> Unit = {}) {
        if (running) {
            result(Result.success("server is already running"))
            LogManager.i("server is already running")
            return
        }
        mServerConfig = serverConfig
        mRouterManager = RouterManager()
        mStatus = Status()
        mServer = serverConfig.server
        mServer?.start { r ->
            result(r)
            when (r) {
                is Result.Success -> {
                    running = true
                    LogManager.i(r.data)
                }
                is Result.Error -> {
                    running = false
                    LogManager.i(r.message)

                }
            }
        } ?: throw RuntimeException("server need an impl")

    }

    @JvmStatic
    @JvmOverloads
    fun stop(result: (Result<String>) -> Unit = {}) {
        if (!running) {
            result(Result.success("server is already stop"))
            LogManager.i("server is already stop")
            return
        }
        mServer?.stop { r ->
            result(r)
            when (r) {
                is Result.Success -> {
                    running = false
                    LogManager.i(r.data)
                }
                is Result.Error -> {
                    running = true
                    LogManager.i(r.message)
                }
            }
        }
    }

}