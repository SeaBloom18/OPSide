package com.ops.opside.common.utils

object ID {

    fun getTemporalId(): String {
        val p = (Math.random() * 25 + 1).toInt()
        val s = (Math.random() * 25 + 1).toInt()
        val t = (Math.random() * 25 + 1).toInt()
        val c = (Math.random() * 25 + 1).toInt()
        val number1 = (Math.random() * 1012 + 2111).toInt()
        val number2 = (Math.random() * 1012 + 2111).toInt()
        val elements = arrayOf(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
        )

        return "Temp@" + elements[p] + elements[s] + number1 + elements[t] + elements[c] + number2
    }

}