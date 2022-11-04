package com.ops.opside.common.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ops.opside.common.entities.room.EventRE

@Dao
interface EventDao {

    @Insert
    fun createEvent(event: EventRE)

    @Query("SELECT * FROM eventre WHERE idTaxCollection = :idTaxCollection")
    fun getAllEvents(idTaxCollection: String): MutableList<EventRE>

    @Delete
    fun deleteEvent(event: EventRE)

}