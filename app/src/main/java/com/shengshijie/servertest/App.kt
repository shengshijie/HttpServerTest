package com.shengshijie.servertest

import android.app.Application
import androidx.multidex.MultiDex
import com.didichuxing.doraemonkit.DoraemonKit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        DoraemonKit.install(this, mutableListOf(),"pId");
    }

}
