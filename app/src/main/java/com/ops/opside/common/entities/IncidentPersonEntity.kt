package com.ops.opside.common.entities

data class IncidentPersonEntity(
    val id: Long,
    var reportName: String,
    var assignName: String,
    var date: String,
    var code: Long,
    var price: Int)