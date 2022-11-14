package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_PENDING_EMAIL

@Entity(tableName = DB_TABLE_PENDING_EMAIL)
data class PendingEmailSE(
    @PrimaryKey(autoGenerate = false)
    var idFirebase: String = "",
    @ColumnInfo(name = "idTaxCollection")
    var idTaxCollection: String = "",
    @ColumnInfo(name = "typeEmail")
    var typeEmail: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "collector")
    var collector: String = "",
    @ColumnInfo(name = "marketName")
    var marketName: String = "",
    @ColumnInfo(name = "email")
    var email: String = "",
    @ColumnInfo(name = "amount")
    var amount: String = "",
    @ColumnInfo(name = "linealMeters")
    var linealMeters: String = "",
    @ColumnInfo(name = "date")
    var date: String = ""
)

const val EMAIL_ABSENCE = "absence"
const val EMAIL_RECEIPT = "receipt"