package com.shengshijie.server.http.config

class Config {

    private val port = 8888

    private val backlog: Int = Constant.DEFAULT_BACKLOG
    private val minWorkers: Int = Constant.DEFAULT_MIN_WORKER_THREAD
    private val maxWorkers: Int = Constant.DEFAULT_MAX_WORKER_THREAD

    private val scanPackages: List<String> = mutableListOf()


}