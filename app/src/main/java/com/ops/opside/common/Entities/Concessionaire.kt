package com.ops.opside.common.Entities

data class Concessionaire(
    val id: String,
    var name: String,
    var address: String = "",
    var phone: String = "",
    var email: String = "",
    var linearMeters: Double = 0.0,
    var lineBusiness: String = "",
    val absence: Int = 0,
    var participantTianguis: MutableList<String> = mutableListOf())