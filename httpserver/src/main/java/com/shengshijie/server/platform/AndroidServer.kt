package com.shengshijie.server.platform

import android.content.Context
import com.shengshijie.server.AbstractServer
import com.shengshijie.server.http.scanner.IPackageScanner

object AndroidServer : AbstractServer() {

    private var mContext: Context? = null

    private var mPackageName: String? = null

    fun setContext(context: Context) {
        mContext = context
    }

    fun setPackageName(packageName: String) {
        mPackageName = packageName
    }

    override fun getPackageScanner(): IPackageScanner {
        return AndroidPackageScanner(mContext!!)
    }

    override fun getPackageName(): String {
        return mPackageName?:""
    }

}