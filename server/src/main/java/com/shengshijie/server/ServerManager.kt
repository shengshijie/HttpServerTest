package com.shengshijie.server

import com.shengshijie.server.common.Result
import com.shengshijie.server.http.common.Constant
import com.shengshijie.server.http.config.ServerConfig
import com.shengshijie.server.http.controller.Status
import com.shengshijie.server.http.router.RouterManager
import com.shengshijie.server.http.serialize.Serialize
import com.shengshijie.server.log.LogManager
import kotlin.concurrent.thread

object ServerManager {

    private var mServer: IServer? = null
    private var running: Boolean = false
    internal var mServerConfig: ServerConfig = ServerConfig.defaultServerConfig
    internal var mLogManager: LogManager = LogManager()
    internal lateinit var mRouterManager: RouterManager
    internal lateinit var mStatus: Status
    internal lateinit var mSerialize: Serialize

    @Volatile
    private var start = false

    @JvmStatic
    @JvmOverloads
    fun start(serverConfig: ServerConfig = ServerConfig.defaultServerConfig, result: (Result<String>) -> Unit = {}) {
        thread {
            if (start) {
                return@thread
            }
            start = true
            if (running) {
                result(Result.success("server is already running"))
                mLogManager.i("server is already running")
                start = false
                return@thread
            }
            mServerConfig = serverConfig
            mLogManager.setLog(mServerConfig.log)
            mRouterManager = RouterManager()
            mStatus = Status()
            mServer = mServerConfig.server
            mSerialize = mServerConfig.serialize
            if (mServerConfig.debug) mServerConfig.packageNameList.add(Constant.DEBUG_CONTROLLER_PACKAGE_NAME)
            mRouterManager.registerRouter(mServerConfig.server.getPackageScanner(), mServerConfig.packageNameList)
            mServer?.start { r ->
                result(r)
                when (r) {
                    is Result.Success -> {
                        running = true
                        mLogManager.i(r.data)
                    }
                    is Result.Error -> {
                        running = false
                        mLogManager.e(r.message)
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
            mLogManager.i("server is already stop")
            return
        }
        mServer?.stop { r ->
            result(r)
            when (r) {
                is Result.Success -> {
                    running = false
                    mLogManager.i(r.data)
                }
                is Result.Error -> {
                    running = true
                    mLogManager.e(r.message)
                }
            }
        }
    }

}