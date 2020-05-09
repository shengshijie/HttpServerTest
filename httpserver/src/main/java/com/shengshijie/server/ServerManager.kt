package com.shengshijie.server

import com.shengshijie.server.http.config.Config

object ServerManager {

    private var mServer: IServer? = null

    fun start(config: Config) {
        mServer = config.server
        mServer?.start(config) ?: throw RuntimeException("server need an impl")
    }

    fun stop() {
        mServer?.stop()
    }

}