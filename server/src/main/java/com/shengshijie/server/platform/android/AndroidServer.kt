package com.shengshijie.server.platform.android

import android.content.Context
import com.shengshijie.server.AbstractServer
import com.shengshijie.server.http.scanner.IPackageScanner

class AndroidServer(mContext: Context) : AbstractServer() {

    var mPackageScanner: IPackageScanner = AndroidPackageScanner(mContext)

    override fun getPackageScanner(): IPackageScanner = mPackageScanner

}