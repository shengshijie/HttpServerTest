package com.shengshijie.server.platform.defaultplatform

import com.shengshijie.server.AbstractServer
import com.shengshijie.server.http.scanner.IPackageScanner

class DefaultServer : AbstractServer() {

    override fun getPackageScanner(): IPackageScanner = DefaultPackageScanner()

}