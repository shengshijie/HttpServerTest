package com.shengshijie.server.log

enum class LogLevel(private val nettyLogLevel: io.netty.handler.logging.LogLevel) {

    TRACE(io.netty.handler.logging.LogLevel.TRACE),
    DEBUG(io.netty.handler.logging.LogLevel.DEBUG),
    INFO(io.netty.handler.logging.LogLevel.INFO),
    WARN(io.netty.handler.logging.LogLevel.WARN),
    ERROR(io.netty.handler.logging.LogLevel.ERROR);

    fun toAndroidLogLevel(): Int {
        return ordinal + 2
    }

    fun toNettyLogLevel(): io.netty.handler.logging.LogLevel {
        return nettyLogLevel
    }

}