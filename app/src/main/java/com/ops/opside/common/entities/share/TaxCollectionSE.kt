package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TaxCollection")
data class TaxCollectionSE(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "idFirebase")
    val idFirebase: String,
    @ColumnInfo(name = "idTianguis")
    var idTianguis: String,
    @ColumnInfo(name = "tianguisName")
    var tianguisName: String,
    @ColumnInfo(name = "totalAmount")
    var totalAmount: Double,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "startTime")
    val startTime: String,
    @ColumnInfo(name = "endTime")
    val endTime: String,
    @ColumnInfo(name = "taxCollector")
    val taxCollector: String,
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TaxCollectionSE

        if (id != other.id) return false
        if (idTianguis != other.idTianguis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + idTianguis.hashCode()
        return result
    }
}
