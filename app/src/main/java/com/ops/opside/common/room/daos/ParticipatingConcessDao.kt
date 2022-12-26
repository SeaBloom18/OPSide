package com.ops.opside.common.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ops.opside.common.entities.share.ParticipatingConcessSE

@Dao
interface ParticipatingConcessDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addConcessionaireToMarket(participatingConcess: ParticipatingConcessSE)

}