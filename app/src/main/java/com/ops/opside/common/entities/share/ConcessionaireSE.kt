package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_CONCESSIONAIRE

@Entity(tableName = DB_TABLE_CONCESSIONAIRE)
data class ConcessionaireSE(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "idFirebase")
    val idFirebase: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "address")
    var address: String = "",
    @ColumnInfo(name = "phone")
    var phone: String = "",
    @ColumnInfo(name = "email")
    var email: String = "",
    @ColumnInfo(name = "linearMeters")
    var linearMeters: Double = 0.0,
    @ColumnInfo(name = "lineBusiness")
    var lineBusiness: String = "",
    @ColumnInfo(name = "absence")
    val absence: Int = 0,
    @ColumnInfo(name = "isForeigner")
    val isForeigner: Boolean = false,
    @ColumnInfo(name = "password")
    var password: String = "",
    @ColumnInfo(name = "origin")
    var origin: String = ""/*,
    @ColumnInfo(name = "participatingMarkets")
    var participatingMarkets: MutableList<String> = mutableListOf()*/
)