package com.ops.opside.common.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object CalendarUtils {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimeStamp(format: String): String{
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date())
    }

}