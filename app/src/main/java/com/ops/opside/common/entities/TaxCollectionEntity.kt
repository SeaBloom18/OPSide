package com.ops.opside.common.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.dataClasses.DealerAssist

@Entity(tableName = "TaxCollectionEntity")
data class TaxCollectionEntity(
    @PrimaryKey(autoGenerate = true) val idCollection: Long = 0,
    var idTianguis: String,
    var tianguisName: String,
    var totalAmount: Double,
    val date: String,
    val startTime: String,
    val endTime: String,
    val taxCollector: String,
    /*val dealers: MutableList<DealerAssist>*/){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaxCollectionEntity

        if (idCollection != other.idCollection) return false
        if (idTianguis != other.idTianguis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idCollection.hashCode()
        result = 31 * result + idTianguis.hashCode()
        return result
    }
}
