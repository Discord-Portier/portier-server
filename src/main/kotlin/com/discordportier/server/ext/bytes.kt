package com.discordportier.server.ext

import org.apache.commons.codec.binary.Hex

fun ByteArray.toHexString(): String =
    Hex.encodeHexString(this)
