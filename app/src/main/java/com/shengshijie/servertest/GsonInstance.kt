package com.shengshijie.servertest

import com.google.gson.Gson
import com.google.gson.GsonBuilder

internal object GsonInstance {

    val gson: Gson = GsonBuilder().serializeNulls().create()

}