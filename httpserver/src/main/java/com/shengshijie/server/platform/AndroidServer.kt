package com.shengshijie.server.platform

import android.content.Context
import com.shengshijie.server.AbstractServer
import com.shengshijie.server.http.scanner.IPackageScanner

class AndroidServer : AbstractServer() {

    private var mContext: Context? = null

    fun setContext(context: Context):AndroidServer {
        mContext = context
        return this
    }

    override fun getPackageScanner(): IPackageScanner {
        return AndroidPackageScanner(mContext!!)
    }

}