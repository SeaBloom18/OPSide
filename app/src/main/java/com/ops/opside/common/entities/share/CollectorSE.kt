package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import java.io.Serializable

/**
 * Created by David Alejandro González Quezada on 10/11/22.
 */
@Entity(tableName = DB_TABLE_COLLECTOR)
data class CollectorSE(
    @PrimaryKey(autoGenerate = false) var idFirebase: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "address") var address: String = "",
    @ColumnInfo(name = "hasAccess") var hasAccess: Boolean = false,
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "phone") var phone: String = ""
): Serializable