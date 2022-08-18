package com.ops.opside.common.entities.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ParticipatingConcess")
data class ParticipatingConcessRE(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "idMarket")
    val idMarket: String,
    @ColumnInfo(name = "idConcessionaire")
    val idConcessionaire: String
)
