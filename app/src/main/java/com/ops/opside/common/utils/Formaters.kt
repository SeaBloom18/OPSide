package com.ops.opside.common.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object Formaters {

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

    fun Int?.orZero(): Int = this ?: 0

    fun Double?.orZero(): Double = this ?: 0.0

    fun Float?.orZero(): Float = this ?: 0f

    fun Long?.orZero(): Long = this ?: 0L

    fun Boolean?.orTrue(): Boolean = this ?: true

    fun Boolean?.orFalse(): Boolean = this ?: false

    fun Double?.formatCurrency(): String {
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.currency = Currency.getInstance("USD")
        return format.format(this.toString().toDouble())
    }

    fun Double?.round(): Double {
        return Math.round(this.orZero() * 100.0) / 100.0
    }

    fun formatDate(strDate: String?): String {
        val formatIn = SimpleDateFormat(FORMAT_SQL_DATE, Locale.getDefault())
        formatIn.timeZone = TimeZone.getTimeZone("UTC")
        val formatter = SimpleDateFormat(FORMAT_DATE, Locale.getDefault())
        val date = strDate?.let { formatIn.parse(it) }

        return if (date == null) {
            ""
        } else {
            formatter.timeZone = TimeZone.getDefault()
            formatter.format(date)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun parseFormat(strDate: String?, previousFormat: String, newFormat: String): String {
        val input = SimpleDateFormat(previousFormat)
        val output = SimpleDateFormat(newFormat)

        try {
            val getAbbreviate = strDate?.let { input.parse(it) }
            return output.format(getAbbreviate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateMillis(millis: Long?): String {
        return if (millis == null) {
            ""
        } else {
            val simpleDateFormat = SimpleDateFormat(FORMAT_DATE)
            simpleDateFormat.format(millis)
        }
    }
}

const val FORMAT_DATE = "dd MMMM, yyyy"
const val FORMAT_SQL_DATE = "yyyy-MM-dd"
const val FORMAT_TIME = "hh:mm a"
const val FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss"