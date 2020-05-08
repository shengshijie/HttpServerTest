package com.shengshijie.server

import io.netty.handler.logging.LogLevel

object ServerManager {

    private var mServer: IServer? = null

    fun setServerImpl(server: IServer) {
        mServer = server
    }

    fun setLogImpl(log: (level: LogLevel, content: String) -> Unit) {
        LogManager.setLogImpl(log)
    }

    fun start(port: Int) {
        mServer?.apply {
            start(port)
        }
    }

    fun stop() {
        mServer?.stop()
    }

}