package com.ops.opside.common.Entities

data class Concessionaire(
    val id: String,
    var name: String,
    var address: String,
    var phone: String,
    var email: String,
    var linearMeters: Double,
    var lineBusiness: String,
    val absence: Int,
    var participantTianguis: MutableList<String>)
