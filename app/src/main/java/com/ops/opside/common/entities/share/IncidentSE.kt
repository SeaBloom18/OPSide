package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_INCIDENT

@Entity(tableName = DB_TABLE_INCIDENT)
data class IncidentSE(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "idFirebase")
    val idFirebase: String,
    @ColumnInfo(name = "incidentName")
    val incidentName: String,
    @ColumnInfo(name = "incidentPrice")
    val incidentPrice: Double
)