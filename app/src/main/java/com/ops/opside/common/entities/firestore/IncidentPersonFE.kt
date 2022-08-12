package com.ops.opside.common.entities.firestore

data class IncidentPersonFE(
    val idFirebase: String,
    var reportName: String,
    var assignName: String,
    var date: String,
    var idIncident: Long,
    var price: Double
)