package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_CONCESSIONAIRE
import com.ops.opside.common.entities.firestore.ConcessionaireFE

@Entity(tableName = DB_TABLE_CONCESSIONAIRE)
data class ConcessionaireSE(
    @PrimaryKey(autoGenerate = false)
    var idFirebase: String = "",
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
    var absence: Int = 0,
    @ColumnInfo(name = "isForeigner")
    var isForeigner: Boolean = false,
    @ColumnInfo(name = "origin")
    var origin: String = "",
    @Ignore var wasPaid: Boolean = false
) {
    constructor() : this(
        idFirebase = "",
        name = "",
        address = "",
        phone = "",
        email = "",
        lineBusiness = "",
        absence = 0,
        isForeigner = false,
        origin = "",
        wasPaid = false
    )

    fun parseToFe(): ConcessionaireFE {
        return ConcessionaireFE(
            idFirebase,
            name,
            address,
            origin,
            phone,
            email,
            role ?: 1,
            0.0,
            lineBusiness ?: "",
            0,
            isForeigner,
            "",
            mutableListOf()
        )
    }
}