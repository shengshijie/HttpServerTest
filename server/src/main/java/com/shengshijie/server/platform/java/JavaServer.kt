package com.shengshijie.server.platform.java

import com.shengshijie.server.AbstractServer
import com.shengshijie.server.http.scanner.IPackageScanner

class JavaServer : AbstractServer() {

    override fun getPackageScanner(): IPackageScanner = JavaPackageScanner()

}