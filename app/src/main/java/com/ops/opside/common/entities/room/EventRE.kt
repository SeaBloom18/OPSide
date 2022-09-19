package com.ops.opside.common.entities.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventRE(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "idTaxCollection")
    val idTaxCollection: String,
    @ColumnInfo(name = "idConcessionaire")
    val idConcessionaire: String,
    @ColumnInfo(name = "nameConcessionaire")
    val nameConcessionaire: String,
    @ColumnInfo(name = "status")
    var status: String,
    @ColumnInfo(name = "timeStamp")
    var timeStamp: String,
    @ColumnInfo(name = "amount")
    var amount: Double = 0.0,
    @ColumnInfo(name = "foreignIdRow")
    var foreignIdRow: String
)
