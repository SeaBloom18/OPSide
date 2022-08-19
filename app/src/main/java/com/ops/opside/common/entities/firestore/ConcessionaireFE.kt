package com.ops.opside.common.entities.firestore

data class ConcessionaireFE(
    val idFirebase: String,
    var name: String,
    var address: String = "",
    var origin: String = "",
    var phone: String = "",
    var email: String = "",
    var linearMeters: Double = 0.0,
    var lineBusiness: String = "",
    val absence: Int = 0,
    val isForeigner: Boolean = false,
    var participatingMarkets: MutableList<String> = mutableListOf()
){
    fun getHashMap(): MutableMap<String,Any>{
        val map: MutableMap<String,Any> = mutableMapOf()

        map["name"] = name
        map["address"] = address
        map["origin"] = origin
        map["phone"] = phone
        map["email"] = email
        map["linearMeters"] = linearMeters
        map["lineBusiness"] = lineBusiness
        map["absence"] = absence
        map["isForeigner"] = isForeigner
        map["participatingMarkets"] = participatingMarkets

        return map
    }
}