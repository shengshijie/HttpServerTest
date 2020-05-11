package com.shengshijie.server

interface IServer {

    fun start(result: (Result<String>) -> Unit)

    fun stop(result: (Result<String>) -> Unit)

}