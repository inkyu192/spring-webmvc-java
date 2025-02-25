package spring.web.kotlin.infrastructure.util

fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }

fun String.hexToBytes() = chunked(2).map { it.toInt(16).toByte() }.toByteArray()