package com.ops.opside.common.room.daos

import androidx.room.*
import com.ops.opside.common.entities.relationships.ConcessionaireWithMarkets
import com.ops.opside.common.entities.relationships.MarketWithConcessionaires
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.entities.share.ConcessionaireSE

@Dao
interface ConcessionaireDao {

    @Query("DELETE FROM concessionaire")
    fun deleteAllConcesionaires()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addConcessionairesList(concessionaire: MutableList<ConcessionaireSE>): MutableList<Long>

    @Insert
    fun addConcessPartipating(participating: MutableList<ParticipatingConcessRE>)

    @Query("DELETE FROM concessionaire WHERE idFirebase IN (:concessionaires)")
    fun deleteConcessInMarket(concessionaires: List<String>)

    @Query("DELETE FROM participatingConcess WHERE idMarket = :idMarket")
    fun deleteConcessPartipating(idMarket: String)

    @Query("SELECT * FROM concessionaire")
    fun getAllConcessionaires(): MutableList<ConcessionaireSE>

    @Transaction
    @Query("SELECT * FROM market WHERE idFirebase = :idMarket")
    fun getAllConcessionairesByMarket(idMarket: String): MarketWithConcessionaires

    @Query("SELECT * FROM participatingConcess")
    fun getAllParticipatingConcess(): MutableList<ParticipatingConcessRE>

    @Query("SELECT * FROM participatingConcess WHERE idMarket = :idMarket")
    fun getAllParticipatingConcessById(idMarket: String): MutableList<ParticipatingConcessRE>

}