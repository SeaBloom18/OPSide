package com.ops.opside.common.entities.share

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_TAX_COLLECTION
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = DB_TABLE_TAX_COLLECTION)
data class TaxCollectionSE(
    @PrimaryKey(autoGenerate = false)
    var idFirebase: String,
    @ColumnInfo(name = "idMarket")
    var idMarket: String,
    @ColumnInfo(name = "marketName")
    var marketName: String,
    @ColumnInfo(name = "totalAmount")
    var totalAmount: Double,
    @ColumnInfo(name = "startDate")
    val startDate: String,
    @ColumnInfo(name = "endDate")
    var endDate: String,
    @ColumnInfo(name = "startTime")
    val startTime: String,
    @ColumnInfo(name = "endTime")
    var endTime: String,
    @ColumnInfo(name = "taxCollector")
    val taxCollector: String,
    @ColumnInfo(name = "idTaxCollector", defaultValue = "")
    val idTaxCollector: String
): Parcelable{

    fun getHashMap(): MutableMap<String,Any>{
        val map: MutableMap<String,Any> = mutableMapOf()
        map["idMarket"] = idMarket
        map["marketName"] = marketName
        map["totalAmount"] = totalAmount
        map["startDate"] = startDate
        map["endDate"] = endDate
        map["startTime"] = startTime
        map["endTime"] = endTime
        map["taxCollector"] = taxCollector
        map["idTaxCollector"] = idTaxCollector

        return map
    }

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
