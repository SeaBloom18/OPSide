package com.ops.opside.common.entities.share

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_INCIDENT
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize
@Entity(tableName = DB_TABLE_INCIDENT)
data class IncidentSE(
    @PrimaryKey(autoGenerate = false) val idFirebase: String,
    @ColumnInfo(name = "incidentName") val incidentName: String,
    @ColumnInfo(name = "incidentPrice") val incidentPrice: Double,
    @ColumnInfo(name = "incidentDescription", defaultValue = "") val incidentDescription: String,
): Serializable, Parcelable {

    fun getHashMap(): MutableMap<String,Any>{
        val map: MutableMap<String,Any> = mutableMapOf()
        map["incidentName"] = incidentName
        map["incidentPrice"] = incidentPrice
        map["incidentDescription"] = incidentDescription

        return map
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncidentSE

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