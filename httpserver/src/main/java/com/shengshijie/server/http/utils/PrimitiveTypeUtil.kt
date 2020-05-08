package com.shengshijie.server.http.utils

import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass

object PrimitiveTypeUtil {

    private val PRI_TYPE = arrayOf<KClass<*>?>(
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
    )

    private val PRI_ARRAY_TYPE = arrayOf<KClass<*>>(
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
    )
    private val primitiveDefaults: MutableMap<KClass<*>?, Any> = HashMap(9)

    fun isPriType(cls: KClass<*>): Boolean {
        for (priType in PRI_TYPE) {
            if (cls == priType) {
                return true
            }
        }
        return false
    }

    fun isPriArrayType(cls: KClass<*>): Boolean {
        for (priType in PRI_ARRAY_TYPE) {
            if (cls == priType) {
                return true
            }
        }
        return false
    }

    fun getPriDefaultValue(type: KClass<*>?): Any? {
        return primitiveDefaults[type]
    }

    init {
        primitiveDefaults[Boolean::class] = false
        primitiveDefaults[Byte::class] = 0.toByte()
        primitiveDefaults[Short::class] = 0.toShort()
        primitiveDefaults[Char::class] = 0.toChar()
        primitiveDefaults[Int::class] = 0
        primitiveDefaults[Long::class] = 0L
        primitiveDefaults[Float::class] = 0.0f
        primitiveDefaults[Double::class] = 0.0
    }
}