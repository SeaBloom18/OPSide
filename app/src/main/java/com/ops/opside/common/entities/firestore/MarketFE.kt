package com.ops.opside.common.entities.firestore

import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.Formaters.orZero

data class MarketFE(
    var idFirebase: String = "",
    var name: String = "",
    var address: String = "",
    var marketMeters: Double = 0.0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var concessionaires: MutableList<String> = mutableListOf(),
    var isDeleted: Boolean = false
){
    fun getHashMap(): MutableMap<String,Any>{
        val map: MutableMap<String,Any> = mutableMapOf()

        map["name"] = name
        map["address"] = address
        map["marketMeters"] = marketMeters
        map["latitude"] = latitude
        map["longitude"] = longitude
        map["concessionaires"] = concessionaires
        map["isDeleted"] = isDeleted

        return map
    }

    fun parseToSE(): MarketSE{
        return MarketSE(
            idFirebase,
            name,
            address,
            marketMeters,
            latitude.orZero(),
            longitude.orZero(),
            concessionaires.toString(),
            isDeleted.not()
        )
    }
}
