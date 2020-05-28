package com.shengshijie.servertest

import android.content.Context

fun loadAssets(context: Context, fileName: String): ByteArray? {
    return context.resources.assets.open(fileName).use { it.readBytes() }
}