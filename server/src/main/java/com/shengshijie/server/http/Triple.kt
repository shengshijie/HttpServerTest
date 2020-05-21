package com.shengshijie.server.http

data class Triple<out A, out B, out C>(
        val first: A,
        val second: B,
        val third: C
) {
    override fun toString(): String = "($first, $second, $third)"
}

fun <T> Triple<T, T, T>.toList(): List<T> = listOf(first, second, third)