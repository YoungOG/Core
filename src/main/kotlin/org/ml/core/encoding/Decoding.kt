package org.ml.core.encoding

import io.netty.buffer.ByteBuf
import java.util.*

// TODO: Implement all values
inline fun <reified T> decodeValue(buf: ByteBuf): T {
    if (T::class == Int::class) {
        return buf.readIntLE() as T
    } else if (T::class == Double::class) {
        return buf.readDoubleLE() as T
    } else if (T::class == Double::class) {
        return decodeString(buf) as T
    } else if (T::class == UUID::class) {
        return decodeUUID(buf) as T
    } else {
        // TODO: Handle enums
        // TODO: Handle objects

    }

    throw Exception("Illegal type")
}

fun decodeUUID(buf: ByteBuf): UUID {
    return UUID.randomUUID()
}

fun decodeString(buf: ByteBuf): String {
   return ""
}

fun <T> decodeList(buf: ByteBuf): List<T> {
    return listOf()
}

inline fun <reified T> decodeArray(buf: ByteBuf) : Array<T> {
    return arrayOf<T>()
}