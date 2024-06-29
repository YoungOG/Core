package org.ml.core.encoding

import io.netty.buffer.ByteBuf
import java.util.UUID

// TODO: Implement all values
fun encodeValue(buf: ByteBuf, value: Any) {
    if (value is Int) {
        buf.writeIntLE(value);
    } else if (value is Double) {
        buf.writeDoubleLE(value)
    } else if (value is String) {
        encodeString(buf, value)
    } else if (value is UUID) {
        encodeUUID(buf, value);
    } else if (value is Array<*>) {
        encodeArray(buf, value)
    } else if (value is List<*>) {
        encodeList(buf, value)
    } else {
        // TODO: Handle enums
        val fields = value::class.java.declaredFields
        for (field in fields) {
            encodeValue(buf, field.get(value))
        }
    }
}

fun encodeUUID(buf: ByteBuf, value: UUID) {
    buf.writeLongLE(value.mostSignificantBits)
    buf.writeLongLE(value.leastSignificantBits)
}

fun encodeString(buf: ByteBuf, value: String) {
    buf.writeShortLE(value.length)
    for (ch in value) {
        buf.writeChar(ch.code);
    }
}

fun encodeList(buf: ByteBuf, value: List<*>) {
    // TODO: Assert length or list and all values are same type
    buf.writeShortLE(value.size)
    for (v in value) {
        if (v == null) {
            throw Exception("Illegal stuff")
        }

        encodeValue(buf, v)    }
}
fun encodeArray(buf: ByteBuf, value: Array<*>) {
    // TODO: Assert length or list and all values are same type
    buf.writeShortLE(value.size)
    for (v in value) {
        if (v == null) {
            throw Exception("Illegal stuff")
        }

        encodeValue(buf, v)
    }
}

