package com.ops.opside.common.entities.firestore

import com.ops.opside.common.entities.share.ConcessionaireSE

data class ConcessionaireFE(
    val idFirebase: String = "",
    var name: String = "",
    var address: String = "",
    var origin: String = "",
    var phone: String = "",
    var email: String = "",
    var role: Int = 0,
    var linearMeters: Double = 0.0,
    var lineBusiness: String = "",
    val absence: Int = 0,
    var isForeigner: Boolean = false,
    var password: String = "",
    var participatingMarkets: MutableList<String> = mutableListOf()
) {
    fun getHashMap(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()

        map["name"] = name
        map["address"] = address
        map["origin"] = origin
        map["phone"] = phone
        map["email"] = email
        map["role"] = role
        map["linearMeters"] = linearMeters
        map["lineBusiness"] = lineBusiness
        map["absence"] = absence
        map["isForeigner"] = isForeigner
        map["password"] = password
        map["participatingMarkets"] = participatingMarkets

        return map
    }

    fun parseToSE(): ConcessionaireSE {
        return ConcessionaireSE(
            idFirebase,
            name,
            address,
            phone,
            email,
            role,
            lineBusiness,
            absence,
            isForeigner,
            origin
        )
    }

}