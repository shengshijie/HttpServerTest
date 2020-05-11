package com.shengshijie.server

import com.shengshijie.server.http.scanner.IPackageScanner

interface IServer {

    fun start(result: (Result<String>) -> Unit)

    fun stop(result: (Result<String>) -> Unit)

    fun getPackageScanner():IPackageScanner

}