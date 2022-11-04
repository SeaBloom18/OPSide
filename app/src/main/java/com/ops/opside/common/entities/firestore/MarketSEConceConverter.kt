package com.ops.opside.common.entities.firestore

import androidx.room.TypeConverter
import com.ops.opside.common.entities.share.MarketSE
import java.util.*

/**
 * Created by David Alejandro González Quezada on 15/10/22.
 */
class MarketSEConceConverter {
    @TypeConverter
    fun storedStringToLanguages(value: String): MarketSE? {
        val langs: MutableList<Array<String>> = Arrays.asList(value.split("\\s*,\\s*").toTypedArray())
        return MarketSE(langs.toString())
    }

    @TypeConverter
    fun languagesToStoredString(cl: MarketSE): String? {
        var value = ""
        for (lang in cl.numberConcessionaires) value += "$lang,"
        return value
    }
}