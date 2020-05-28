package com.shengshijie.server.http.utils

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

internal object PrimitiveTypeUtil {

    fun isPriType(cls: KClass<*>): Boolean {
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
                BigInteger::class,
                BigDecimal::class
        ).any { cls == it }
    }

    fun isPriArrayType(cls: KClass<*>): Boolean {
        return arrayOf<Any>(
                BooleanArray::class,
                ByteArray::class,
                ShortArray::class,
                IntArray::class,
                LongArray::class,
                FloatArray::class,
                DoubleArray::class,
                CharArray::class,
                Array<String>::class,
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
        ).any { cls == it }
    }

    fun convert(content: String, clazz: KClass<*>): Any {
        when (clazz) {
            String::class -> {
                return content
            }
            Boolean::class -> {
                return content.toBoolean()
            }
            Byte::class -> {
                return content.toByte()
            }
            Short::class -> {
                return content.toShort()
            }
            Int::class -> {
                return content.toInt()
            }
            Long::class -> {
                return content.toLong()
            }
            Float::class -> {
                return content.toFloat()
            }
            Double::class -> {
                return content.toDouble()
            }
            Char::class -> {
                return content.toCharArray()
            }
            BigInteger::class -> {
                return content.toBigInteger()
            }
            BigDecimal::class -> {
                return content.toBigDecimal()
            }
        }
        return content
    }

}