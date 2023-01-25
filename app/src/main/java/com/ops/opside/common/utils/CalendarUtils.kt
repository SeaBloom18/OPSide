package com.ops.opside.common.utils

import android.annotation.SuppressLint
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtils {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimeStamp(format: String): String{
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date())
    }

    fun getCurrentTimeInMillis(): Long {
        return Date().time.minus(DAY_IN_MILLIS)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeStampFromStr(string: String): Timestamp {
        val format = SimpleDateFormat(FORMAT_TIMESTAMP)
        val date: Date = format.parse(string)
        return Timestamp(date)
    }

    fun getCurrentMonth(getCurrent: Boolean, selectedMonth: Int = 0, selectedYear: Int = 0): Pair<Date,Date>{
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()

        val month = if (getCurrent) calendarStart.get(Calendar.MONTH) else selectedMonth
        val year  = if (getCurrent) calendarStart.get(Calendar.YEAR)  else selectedYear

        val startOfMonth = calendarStart.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endOfMonth = calendarEnd.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        return Pair(startOfMonth.time,endOfMonth.time)
    }
}

const val DAY_IN_MILLIS = 86400000