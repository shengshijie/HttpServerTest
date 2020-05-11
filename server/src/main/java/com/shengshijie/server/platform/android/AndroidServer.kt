package com.shengshijie.server.platform.android

import android.content.Context
import com.shengshijie.server.AbstractServer
import com.shengshijie.server.http.scanner.IPackageScanner

class AndroidServer(private val mContext: Context) : AbstractServer() {

    override fun getPackageScanner(): IPackageScanner = AndroidPackageScanner(mContext)

}