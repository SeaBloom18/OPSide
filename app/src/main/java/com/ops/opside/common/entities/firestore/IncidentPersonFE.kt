package com.ops.opside.common.entities.firestore

data class IncidentPersonFE(
    var idFirebase: String = "",
    var idCollector: String = "",
    var reportName: String = "",
    var assignName: String = "",
    var date: String = "",
    var time: String = "",
    var idIncident: String = "",
    var price: Double = 0.0,
    var idTaxCollection: String = "",
    var idConcessionaire: String = ""

) {
    fun getHashMap(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()
        map["idCollector"] = idCollector
        map["reportName"] = reportName
        map["assignName"] = assignName
        map["date"] = date
        map["time"] = time
        map["price"] = price
        map["idIncident"] = idIncident
        map["idTaxCollection"] = idTaxCollection
        map["idConcessionaire"] = idConcessionaire
        return map
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncidentPersonFE

        if (idFirebase != other.idFirebase) return false
        if (idCollector != other.idCollector) return false
        if (reportName != other.reportName) return false
        if (assignName != other.assignName) return false
        if (date != other.date) return false
        if (time != other.time) return false
        if (idIncident != other.idIncident) return false
        if (price != other.price) return false
        if (idTaxCollection != other.idTaxCollection) return false
        if (idConcessionaire != other.idConcessionaire) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idFirebase.hashCode()
        result = 31 * result + idCollector.hashCode()
        result = 31 * result + reportName.hashCode()
        result = 31 * result + assignName.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + idIncident.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + idTaxCollection.hashCode()
        result = 31 * result + idConcessionaire.hashCode()
        return result
    }


}

