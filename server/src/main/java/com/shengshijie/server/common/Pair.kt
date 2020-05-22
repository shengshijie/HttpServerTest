package com.shengshijie.server.common

data class Pair<out A, out B>(
        val first: A,
        val second: B
) {
    override fun toString(): String = "($first, $second)"
}

infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)
fun <T> kotlin.Pair<T, T>.toList(): List<T> = listOf(first, second)

