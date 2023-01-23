package com.ops.opside.common.entities.firestore

data class IncidentPersonFE(
    var incidentName: String = "",
    var idFirebase: String = "",
    var idCollector: String = "",
    var reportName: String = "",
    var assignName: String = "",
    var date: String = "",
    var idIncident: String = "",
    var price: Double = 0.0,
    var idTaxCollection: String = ""

) {
    fun getHashMap(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()
        map["incidentName"] = incidentName
        map["idCollector"] = idCollector
        map["reportName"] = reportName
        map["assignName"] = assignName
        map["date"] = date
        map["price"] = price
        map["idIncident"] = idIncident
        map["idTaxCollection"] = idTaxCollection
        return map
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncidentPersonFE

        if (incidentName != other.incidentName) return false
        if (idFirebase != other.idFirebase) return false
        if (idCollector != other.idCollector) return false
        if (reportName != other.reportName) return false
        if (assignName != other.assignName) return false
        if (date != other.date) return false
        if (idIncident != other.idIncident) return false
        if (price != other.price) return false
        if (idTaxCollection != other.idTaxCollection) return false

        return true
    }

    override fun hashCode(): Int {
        var result = incidentName.hashCode()
        result = 31 * result + idFirebase.hashCode()
        result = 31 * result + idCollector.hashCode()
        result = 31 * result + reportName.hashCode()
        result = 31 * result + assignName.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + idIncident.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + idTaxCollection.hashCode()
        return result
    }

}

