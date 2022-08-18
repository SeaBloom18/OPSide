package com.ops.opside.common.entities.firestore

data class ForeignerAssistenceFE(
    val idFirebase: String,
    val email: String,
    val name: String,
    val linearMetersUsed: Double,
    val dateAssistence: String,
    val idMarket: String
)
