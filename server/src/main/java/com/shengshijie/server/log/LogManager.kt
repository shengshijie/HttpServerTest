package com.shengshijie.server.log

import com.shengshijie.server.ServerManager

internal class LogManager {

    private var mLog: (level: LogLevel, content: String) -> Unit = defaultLog

    fun setLog(log: (level: LogLevel, content: String) -> Unit) {
        mLog = log
    }

    fun v(msg: String) {
        if (ServerManager.mServerConfig.logLevel <= LogLevel.TRACE) mLog(LogLevel.TRACE, msg)
    }

    fun d(msg: String) {
        if (ServerManager.mServerConfig.logLevel <= LogLevel.DEBUG) mLog(LogLevel.DEBUG, msg)
    }

    fun i(msg: String) {
        if (ServerManager.mServerConfig.logLevel <= LogLevel.INFO) mLog(LogLevel.INFO, msg)
    }

    fun w(msg: String) {
        if (ServerManager.mServerConfig.logLevel <= LogLevel.WARN) mLog(LogLevel.WARN, msg)
    }

    fun e(msg: String) {
        if (ServerManager.mServerConfig.logLevel <= LogLevel.ERROR) mLog(LogLevel.ERROR, msg)
    }

    companion object{
        val defaultLog: (level: LogLevel, content: String) -> Unit = { l, s -> println("<${l.name}> $s") }
    }

}