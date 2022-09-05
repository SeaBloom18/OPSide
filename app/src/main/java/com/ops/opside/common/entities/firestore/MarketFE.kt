package com.ops.opside.common.entities.firestore

import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.Formaters.orZero

data class MarketFE(
    val idFirebase: String = "",
    var name: String = "",
    var address: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var concessionaires: MutableList<String> = mutableListOf()
){
    fun getHashMap(): MutableMap<String,Any>{
        val map: MutableMap<String,Any> = mutableMapOf()

        map["name"] = name
        map["address"] = address
        map["latitude"] = latitude
        map["longitude"] = longitude
        map["concessionaires"] = concessionaires

        return map
    }

    fun parseToSE(): MarketSE{
        return MarketSE(
            idFirebase,
            name,
            address,
            latitude.orZero(),
            longitude.orZero(),
            concessionaires.size.orZero()
        )
    }
}
