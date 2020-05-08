package com.shengshijie.server

import io.netty.handler.logging.LogLevel

object LogManager {

    private var mLog: (level: LogLevel, content: String) -> Unit = { _, _ -> }

    fun setLogImpl(log: (level: LogLevel, content: String) -> Unit) {
        mLog = log
    }

    fun v(msg: String) {
        mLog(LogLevel.TRACE, msg)
    }

    fun d(msg: String) {
        mLog(LogLevel.DEBUG, msg)
    }

    fun i(msg: String) {
        mLog(LogLevel.INFO, msg)
    }

    fun w(msg: String) {
        mLog(LogLevel.WARN, msg)
    }

    fun e(msg: String) {
        mLog(LogLevel.ERROR, msg)
    }

}