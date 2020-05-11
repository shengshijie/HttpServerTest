package com.shengshijie.server

import io.netty.handler.logging.LogLevel

object LogManager {

    private var mLog: (level: LogLevel, content: String) -> Unit = { l, s -> println(s) }

    fun setLogImpl(log: (level: LogLevel, content: String) -> Unit) {
        mLog = log
    }

    fun v(msg: String) {
        if (ServerManager.mServerConfig.logLevel.ordinal <= LogLevel.TRACE.ordinal) mLog(LogLevel.TRACE, msg)
    }

    fun d(msg: String) {
        if (ServerManager.mServerConfig.logLevel.ordinal <= LogLevel.DEBUG.ordinal) mLog(LogLevel.DEBUG, msg)
    }

    fun i(msg: String) {
        if (ServerManager.mServerConfig.logLevel.ordinal <= LogLevel.INFO.ordinal) mLog(LogLevel.INFO, msg)
    }

    fun w(msg: String) {
        if (ServerManager.mServerConfig.logLevel.ordinal <= LogLevel.WARN.ordinal) mLog(LogLevel.WARN, msg)
    }

    fun e(msg: String) {
        if (ServerManager.mServerConfig.logLevel.ordinal <= LogLevel.ERROR.ordinal) mLog(LogLevel.ERROR, msg)
    }

}