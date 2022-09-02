package com.ops.opside.common.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ops.opside.common.entities.share.MarketSE

@Dao
interface MarketDao {

    @Query("SELECT idFirebase FROM market WHERE idFirebase = :idMarket")
    fun existMarket(idMarket: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMarket(idMarket: MarketSE)

}