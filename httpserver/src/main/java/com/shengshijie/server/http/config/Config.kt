package com.shengshijie.server.http.config

import com.shengshijie.server.IServer
import com.shengshijie.server.http.controller.Status
import com.shengshijie.server.platform.AndroidServer
import io.netty.handler.logging.LogLevel

object Config {

    fun getConfig(): HashMap<String, String> = hashMapOf(
            "debug" to debug.toString(),
            "logLevel" to logLevel.toString(),
            "port" to port.toString(),
            "backlog" to backlog.toString(),
            "minWorkers" to minWorkers.toString(),
            "maxWorkers" to maxWorkers.toString(),
            "tcpNodelay" to tcpNodelay.toString(),
            "soKeepalive" to soKeepalive.toString(),
            "soReuseaddr" to soReuseaddr.toString(),
            "soRcvbuf" to soRcvbuf.toString(),
            "soSndbuf" to soSndbuf.toString(),
            "packageNameList" to packageNameList.toString())

    var port = 8888
    var debug: Boolean = true
    var logLevel: LogLevel = LogLevel.DEBUG

    var backlog: Int = Constant.DEFAULT_BACKLOG
    var minWorkers: Int = Constant.DEFAULT_MIN_WORKER_THREAD
    var maxWorkers: Int = Constant.DEFAULT_MAX_WORKER_THREAD

    var tcpNodelay: Boolean = Constant.DEFAULT_TCP_NODELAY
    var soKeepalive: Boolean = Constant.DEFAULT_SO_KEEPALIVE
    var soReuseaddr: Boolean = Constant.DEFAULT_SO_REUSEADDR
    var soRcvbuf: Int = Constant.DEFAULT_SO_RCVBUF
    var soSndbuf: Int = Constant.DEFAULT_SO_SNDBUF

    var log: (level: LogLevel, content: String) -> Unit = { l, s -> println(s) }
    var packageNameList: MutableList<String> = mutableListOf()
    var server: IServer? = null

}