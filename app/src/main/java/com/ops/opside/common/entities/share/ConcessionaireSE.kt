package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Concessionaire")
data class ConcessionaireSE(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "idFirebase")
    val idFirebase: String,
    @ColumnInfo(name = "name")
    var name: String,
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
    val password: String = ""
)