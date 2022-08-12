package com.ops.opside.common.entities.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EmailSent")
data class EmailSentRE(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "idConcessionaire")
    val idConcessionaire: Long,
    @ColumnInfo(name = "status")
    val status: Boolean,
    @ColumnInfo(name = "emailType")
    val emailType: String
)
