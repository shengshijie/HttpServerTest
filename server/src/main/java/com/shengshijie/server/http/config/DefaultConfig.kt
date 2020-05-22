package com.shengshijie.server.http.config

import com.shengshijie.server.http.serialize.GsonSerialize
import com.shengshijie.server.log.LogLevel
import com.shengshijie.server.log.LogManager
import com.shengshijie.server.platform.java.JavaServer

internal object DefaultConfig {

    val DEFAULT_LOG_LEVEL = LogLevel.DEBUG
    val DEFAULT_DEFAULT_LOG = LogManager.defaultLog
    val DEFAULT_PACKAGE_LIST = mutableListOf<String>()
    val DEFAULT_SERVER = JavaServer()
    val DEFAULT_SERIALIZER = GsonSerialize()

    const val DEFAULT_ROOT_PATH = ""
    const val DEFAULT_SALT = "salt"
    const val DEFAULT_PORT = 8888
    const val DEFAULT_DEBUG = false
    const val DEFAULT_BACKLOG = 1024
    const val DEFAULT_ENABLE_SSL = false
    const val DEFAULT_ENABLE_CORS = false
    const val DEFAULT_ENABLE_SIGN = false
    const val DEFAULT_TCP_NODELAY = true
    const val DEFAULT_SO_REUSEADDR = true
    const val DEFAULT_SO_KEEPALIVE = false
    const val DEFAULT_SO_RCVBUF = 2048
    const val DEFAULT_SO_SNDBUF = 2048
    const val DEFAULT_MAX_CONTENT_LENGTH = 1024 * 1024 * 5
    const val DEFAULT_CORE_POOL_SIZE = 100
    const val DEFAULT_MAXIMUM_POOL_SIZE = 100
    const val DEFAULT_WORK_QUEUE_CAPACITY = 10000
    const val DEFAULT_SUCCESS_CODE = 1000
    const val DEFAULT_ERROR_CODE_BUSINESS = 2001

}