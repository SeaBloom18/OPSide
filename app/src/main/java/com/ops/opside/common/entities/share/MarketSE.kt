package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Market")
data class MarketSE(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "idFirebase")
    val idFirebase: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "address")
    var address: String,
    @ColumnInfo(name = "latitude")
    var latitude: Double,
    @ColumnInfo(name = "longitude")
    var longitude: Double,
    @ColumnInfo(name = "numberConcessionaires")
    var numberConcessionaires: Int
)