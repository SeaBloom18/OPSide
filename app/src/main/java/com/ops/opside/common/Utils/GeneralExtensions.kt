package com.ops.opside.common.Utils

inline fun tryOrPrintException(f: () -> Unit) {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

