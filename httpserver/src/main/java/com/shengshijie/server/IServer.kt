package com.shengshijie.server

import com.shengshijie.server.http.config.Config

interface IServer {

    fun start(config: Config)

    fun stop()

}