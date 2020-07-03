package com.shengshijie.servertest

import android.app.Application
import androidx.multidex.MultiDex

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
    }

}
