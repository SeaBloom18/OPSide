package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_MARKET
import java.io.Serializable

@Entity(tableName = DB_TABLE_MARKET)
data class MarketSE(
    @PrimaryKey(autoGenerate = false)
    val idFirebase: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "address")
    var address: String = "",
    @ColumnInfo(name = "latitude")
    var latitude: Double = 0.0,
    @ColumnInfo(name = "longitude")
    var longitude: Double = 0.0,
    @ColumnInfo(name = "numberConcessionaires")
    var numberConcessionaires: Int = 0
): Serializable