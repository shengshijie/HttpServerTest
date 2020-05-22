package com.shengshijie.server.http.serialize

import com.google.gson.Gson
import com.google.gson.GsonBuilder

internal object GsonInstance {

    internal val gson: Gson = GsonBuilder().serializeNulls().create()

}