package com.ops.opside.common.entities.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.ops.opside.common.entities.DB_TABLE_PARTICIPATING_CONCESS

@Entity(tableName = DB_TABLE_PARTICIPATING_CONCESS,
primaryKeys = ["idMarket","idConcessionaire"])
data class ParticipatingConcessRE(
    @ColumnInfo(name = "idMarket")
    val idMarket: String,
    @ColumnInfo(name = "idConcessionaire")
    val idConcessionaire: String,
    @ColumnInfo(name = "idFirebase")
    val idFirebase: String,
    @ColumnInfo(name = "linearMeters")
    var linearMeters: Double,
    @ColumnInfo(name = "lineBusiness")
    var lineBusiness: String
)
