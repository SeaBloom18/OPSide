package com.ops.opside.common.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

inline fun tryOrPrintException(f: () -> Unit) {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun TextInputEditText.clear(){
    text?.clear()
}

fun TextInputLayout.error(){
    error = null
}

