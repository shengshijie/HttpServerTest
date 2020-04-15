package com.shengshijie.httpserver

import java.util.*

class Path(requestMapping: RequestMapping, rootRequestMapping: RequestMapping) {
    var method: String = requestMapping.method
    var uri: String = rootRequestMapping.value + requestMapping.value
    var isEqual: Boolean = requestMapping.equal

    override fun toString(): String {
        return method.toUpperCase(Locale.getDefault()) + " " + uri.toUpperCase(Locale.getDefault())
    }

    override fun hashCode(): Int {
        return ("HTTP " + method.toUpperCase(Locale.getDefault()) + " " + uri.toUpperCase(Locale.getDefault())).hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Path) {
            return method.equals(other.method, ignoreCase = true) && uri.equals(other.uri, ignoreCase = true)
        }
        return false
    }

    companion object {
        fun make(requestMapping: RequestMapping, rootRequestMapping: RequestMapping): Path {
            return Path(requestMapping, rootRequestMapping)
        }
    }

}