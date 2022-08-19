package com.ops.opside.common.entities.firestore

data class MarketFE(
    val idFirebase: String,
    var name: String,
    var address: String,
    var latitude: Double,
    var longitude: Double,
    var concessionaires: MutableList<String>
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
}
