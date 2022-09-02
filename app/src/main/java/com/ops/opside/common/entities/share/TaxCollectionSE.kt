package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_TAX_COLLECTION

@Entity(tableName = DB_TABLE_TAX_COLLECTION)
data class TaxCollectionSE(
    @PrimaryKey(autoGenerate = false)
    val idFirebase: String,
    @ColumnInfo(name = "idMarket")
    var idMarket: String,
    @ColumnInfo(name = "marketName")
    var marketName: String,
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

        if (idFirebase != other.idFirebase) return false
        if (idMarket != other.idMarket) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idFirebase.hashCode()
        result = 31 * result + idMarket.hashCode()
        return result
    }
}
