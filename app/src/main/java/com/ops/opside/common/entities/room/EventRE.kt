package com.ops.opside.common.entities.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventRE(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "idTaxCollection")
    var idTaxCollection: String,
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
){
    fun getHashMap(): MutableMap<String,Any>{
        val map: MutableMap<String,Any> = mutableMapOf()

        map["idConcessionaire"] = idConcessionaire
        map["nameConcessionaire"] = nameConcessionaire
        map["status"] = status
        map["timeStamp"] = timeStamp
        map["amount"] = amount
        map["foreignIdRow"] = foreignIdRow

        return map
    }
}
