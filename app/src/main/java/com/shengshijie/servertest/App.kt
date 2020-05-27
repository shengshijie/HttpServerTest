package com.shengshijie.servertest

import android.app.Application
import androidx.multidex.MultiDex
import com.didichuxing.doraemonkit.DoraemonKit

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        DoraemonKit.install(this, mutableListOf(), "pId");
    }

}
