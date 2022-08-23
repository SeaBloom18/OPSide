package com.ops.opside.common.entities.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_PARTICIPATING_CONCESS

@Entity(tableName = DB_TABLE_PARTICIPATING_CONCESS)
data class ParticipatingConcessRE(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "idMarket")
    val idMarket: String,
    @ColumnInfo(name = "idConcessionaire")
    val idConcessionaire: String
)
