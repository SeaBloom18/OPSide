package com.ops.opside.common.Utils

import android.util.Log
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Formaters {

    companion object{
        fun formatDate(date: String): String {

            val simpleFormat: Date = try {
                SimpleDateFormat("yyyy-dd-MM").parse(date) as Date
            } catch (e: ParseException) {
                Log.e("Error", "Error Formating", e)
                return "Error"
            }

            val formatter =
                SimpleDateFormat("dd MMMM, yyyy", Locale("es", "ES"))

            return formatter.format(simpleFormat)
        }

        fun formatMoney(totalAmount: Double): String {
            return try {
                val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
                numberFormat.format(totalAmount)
            } catch (e: Exception) {
                totalAmount.toString()
            }
        }
    }

}