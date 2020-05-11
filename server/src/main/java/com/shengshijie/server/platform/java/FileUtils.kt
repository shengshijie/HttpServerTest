package com.shengshijie.server.platform.java

import java.io.File
import java.util.*

object FileUtils {

    fun listFiles(dir: File, suffix: String, recursive: Boolean): List<File> {
        val files: MutableList<File> = ArrayList()
        if (!dir.isDirectory) return files
        dir.listFiles { f: File -> f.isDirectory || f.isFile && f.name.endsWith(suffix) }?.forEach { childFile ->
            if (childFile.isFile) files.add(childFile)
            files.addAll(listFiles(childFile, suffix, recursive))
        }
        return files
    }

}