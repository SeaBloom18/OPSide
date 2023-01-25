package com.ops.opside.common.entities.firestore

/**
 * Created by davidgonzalez on 23/01/23
 */
data class IncidentFE(
    val idFirebase: String = "",
    var incidentName: String = "",
    var incidentPrice: String = "",
    var incidentDescription: String = ""
) {

    fun getHashMap(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()
        map["incidentName"] = incidentName
        map["incidentPrice"] = incidentPrice
        map["incidentDescription"] = incidentDescription
        return map
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncidentFE

        if (idFirebase != other.idFirebase) return false
        if (incidentName != other.incidentName) return false
        if (incidentPrice != other.incidentPrice) return false
        if (incidentDescription != other.incidentDescription) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idFirebase.hashCode()
        result = 31 * result + incidentName.hashCode()
        result = 31 * result + incidentPrice.hashCode()
        result = 31 * result + incidentDescription.hashCode()
        return result
    }
}
