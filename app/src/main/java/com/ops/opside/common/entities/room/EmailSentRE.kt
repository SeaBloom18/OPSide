package com.ops.opside.common.entities.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_EMAIL_SENT

@Entity(tableName = DB_TABLE_EMAIL_SENT)
data class EmailSentRE(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "idConcessionaire")
    val idConcessionaire: String,
    @ColumnInfo(name = "status")
    val status: Boolean,
    @ColumnInfo(name = "emailType")
    val emailType: String
)
