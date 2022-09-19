package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_CONCESSIONAIRE

@Entity(tableName = DB_TABLE_CONCESSIONAIRE)
data class ConcessionaireSE(
    @PrimaryKey(autoGenerate = false)
    val idFirebase: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "address")
    var address: String = "",
    @ColumnInfo(name = "phone")
    var phone: String = "",
    @ColumnInfo(name = "email")
    var email: String = "",
    @ColumnInfo(name = "role")
    var role: Int = 0,
    @ColumnInfo(name = "lineBusiness")
    var lineBusiness: String = "",
    @ColumnInfo(name = "absence")
    val absence: Int = 0,
    @ColumnInfo(name = "isForeigner")
    val isForeigner: Boolean = false,
    @ColumnInfo(name = "origin")
    var origin: String = ""
)