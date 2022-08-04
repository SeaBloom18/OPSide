package com.ops.opside.common.entities

data class IncidentEntity(
    val id: Long,
    val incidentName: String,
    val incidentPrice: Int,
    val incidentCode: Long)