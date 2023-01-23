package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_INCIDENT
import java.io.Serializable

@Entity(tableName = DB_TABLE_INCIDENT)
data class IncidentSE(
    @PrimaryKey(autoGenerate = false)
    val idFirebase: String,
    @ColumnInfo(name = "incidentName")
    val incidentName: String,
    @ColumnInfo(name = "incidentPrice")
    val incidentPrice: Double
): Serializable