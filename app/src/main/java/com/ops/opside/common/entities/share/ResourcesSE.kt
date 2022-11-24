package com.ops.opside.common.entities.share

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ops.opside.common.entities.DB_TABLE_RESOURCES

@Entity(tableName = DB_TABLE_RESOURCES)
data class ResourcesSE(
    @PrimaryKey(autoGenerate = false)
    val idFirebase: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String
)
