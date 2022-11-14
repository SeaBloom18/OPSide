package com.ops.opside.common.room.daos

import androidx.room.*
import com.ops.opside.common.entities.room.EventRE

@Dao
interface EventDao {

    @Insert
    fun createEvent(event: EventRE)

    @Query("SELECT * FROM eventre WHERE idTaxCollection = :idTaxCollection")
    fun getAllEventsById(idTaxCollection: String): MutableList<EventRE>


    @Query("SELECT * FROM eventre")
    fun getAllEvents(): MutableList<EventRE>

    @Delete
    fun deleteEvent(event: EventRE)

    @Update
    fun updateEvent(event: EventRE)
}