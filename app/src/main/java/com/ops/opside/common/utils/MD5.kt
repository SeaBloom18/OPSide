package com.ops.opside.common.utils

import java.util.zip.CRC32


object MD5 {
    fun hashString(string: String): String{
        val crc32 = CRC32()
        crc32.update(string.toByteArray())
        return String.format("%08X", crc32.value)
    }
}
