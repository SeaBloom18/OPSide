package com.ops.opside.common.Utils

import java.text.DecimalFormat
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
        val formatter = DecimalFormat("MXN/ #,##0.00")
        return formatter.format(this.orZero())
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

    fun formatDateMillis(millis: Long?): String {
        return if (millis == null) {
            ""
        } else {
            val simpleDateFormat = SimpleDateFormat(FORMAT_DATE)
            simpleDateFormat.format(millis)
        }
    }

    const val FORMAT_DATE = "dd MMMM, yyyy"
}