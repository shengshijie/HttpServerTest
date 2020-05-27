package com.shengshijie.servertest

import android.content.Context
import android.content.res.AssetManager
import java.io.ByteArrayOutputStream
import java.io.IOException

fun loadAssets(context: Context, fileName: String): ByteArray? {
    return context.resources.assets.open(fileName).use { it.readBytes() }
}