package com.ops.opside.common.utils

import android.annotation.SuppressLint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Formaters {

    fun Int?.orZero(): Int = this ?: 0

    fun Double?.orZero(): Double = this ?: 0.0

    fun Float?.orZero(): Float = this ?: 0f

    fun Long?.orZero(): Long = this ?: 0L

    fun Boolean?.orTrue(): Boolean = this ?: true

    fun Boolean?.orFalse(): Boolean = this ?: false

    fun Double?.formatCurrency(): String {
        val cleanString = this.toString().replace("""[$,.]""".toRegex(), "")
        val parsed = cleanString.toDouble()
        return NumberFormat.getCurrencyInstance().format((parsed / 100)).toString()
    }

    fun formatDate(strDate: String?): String {
        val formatIn = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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
    fun formatDateMillis(millis: Long?): String {
        return if (millis == null) {
            ""
        } else {
            val simpleDateFormat = SimpleDateFormat(FORMAT_DATE)
            simpleDateFormat.format(millis)
        }
    }

    private const val FORMAT_DATE = "dd MMMM, yyyy"
}