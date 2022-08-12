package com.ops.opside.common.entities.firestore

data class CollectorFE(
    val idFirebase: String,
    var name: String,
    var address: String,
    var phone: String,
    var email: String,
    var role: Int,
    var hasAccess: Boolean,
    var password: String
)
