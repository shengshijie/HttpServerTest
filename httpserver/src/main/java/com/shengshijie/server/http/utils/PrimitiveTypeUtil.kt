package com.shengshijie.server.http.utils

import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass

object PrimitiveTypeUtil {

    fun isPriType(cls: Any): Boolean {
        return arrayOf<Any>(
                String::class,
                Boolean::class,
                Byte::class,
                Short::class,
                Int::class,
                Long::class,
                Float::class,
                Double::class,
                Char::class,
                Boolean::class,
                Byte::class,
                Short::class,
                Int::class,
                Long::class,
                Float::class,
                Double::class,
                Char::class,
                BigInteger::class,
                BigDecimal::class
        ).any { cls.javaClass.kotlin == it }
    }

    fun isPriArrayType(cls: Any): Boolean {
        return arrayOf<Any>(
                Array<String>::class,
                BooleanArray::class,
                ByteArray::class,
                ShortArray::class,
                IntArray::class,
                LongArray::class,
                FloatArray::class,
                DoubleArray::class,
                CharArray::class,
                Array<Boolean>::class,
                Array<Byte>::class,
                Array<Short>::class,
                Array<Int>::class,
                Array<Long>::class,
                Array<Float>::class,
                Array<Double>::class,
                Array<Char>::class,
                Array<BigInteger>::class,
                Array<BigDecimal>::class
        ).any { cls.javaClass.kotlin == it }
    }

}