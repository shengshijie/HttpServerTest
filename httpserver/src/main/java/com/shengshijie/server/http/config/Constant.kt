package com.shengshijie.server.http.config

object Constant {

    const val DEFAULT_BACKLOG = 1024

    const val DEFAULT_TCP_NODELAY = true
    const val DEFAULT_SO_REUSEADDR = true
    const val DEFAULT_SO_KEEPALIVE = false

    const val DEFAULT_SO_RCVBUF = 2048
    const val DEFAULT_SO_SNDBUF = 2048
    const val DEFAULT_MAX_CONTENT_LENGTH = 1024 * 1024 * 5

    const val DEFAULT_CORE_POOL_SIZE = 100
    const val DEFAULT_MAXIMUM_POOL_SIZE = 100
    const val DEFAULT_WORK_QUEUE_CAPACITY = 10000

}