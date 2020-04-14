package com.shengshijie.httpserver

import java.util.*

class Path(annotation: RequestMapping) {
    var method: String = annotation.method
    var uri: String = annotation.path
    var isEqual: Boolean = annotation.equal

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
        fun make(annotation: RequestMapping): Path {
            return Path(annotation)
        }
    }

}