package com.shengshijie.server.http.utils

import java.io.PrintWriter
import java.io.StringWriter

internal object ExceptionUtils {

    fun toString(ex: Throwable): String {
        val strWriter = StringWriter()
        ex.printStackTrace(PrintWriter(strWriter, true))
        return strWriter.toString()
    }

}