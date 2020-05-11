package com.shengshijie.server

import com.shengshijie.server.http.config.Constant
import com.shengshijie.server.http.config.ServerConfig
import com.shengshijie.server.http.controller.Status
import com.shengshijie.server.http.router.RouterManager
import com.shengshijie.server.log.LogManager
import kotlin.concurrent.thread

object ServerManager {

    private var mServer: IServer? = null
    internal var mServerConfig: ServerConfig = ServerConfig.defaultServerConfig
    internal lateinit var mRouterManager: RouterManager
    internal lateinit var mStatus: Status
    private var running: Boolean = false
    @Volatile
    private var start = false

    @JvmStatic
    @JvmOverloads
    fun start(serverConfig: ServerConfig, result: (Result<String>) -> Unit = {}) {
        thread {
            if (start) {
                return@thread
            }
            start = true
            LogManager.setLogImpl(serverConfig.log)
            if (running) {
                result(Result.success("server is already running"))
                LogManager.i("server is already running")
                start = false
                return@thread
            }
            mServerConfig = serverConfig
            mRouterManager = RouterManager()
            mStatus = Status()
            mServer = serverConfig.server
            if (mServerConfig.debug) mServerConfig.packageNameList.add(Constant.DEBUG_CONTROLLER_PACKAGE_NAME)
            mRouterManager.registerRouter(serverConfig.server.getPackageScanner(), mServerConfig.packageNameList)
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
            start = false
        }
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