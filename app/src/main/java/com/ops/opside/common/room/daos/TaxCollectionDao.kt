package com.ops.opside.common.room.daos

import androidx.room.*
import com.ops.opside.common.entities.share.TaxCollectionSE

/*@Dao
interface TaxCollectionDao {

    *//*@Query("SELECT * FROM TaxCollection")
    fun getAllTaxes(): MutableList<TaxCollectionSE>

    @Query("SELECT * FROM TaxCollection where id = :idCollection")
    fun getTaxById(idCollection: Long): TaxCollectionSE

    @Delete
    fun deleteTaxCollection(taxCollection: TaxCollectionSE)*//*

    @Insert
    fun addTaxCollection(taxCollection: TaxCollectionSE): Long

    @Update
    fun updateTaxCollection(taxCollection: TaxCollectionSE)

    @Query("SELECT * FROM taxCollection WHERE idMarket = :idMarket")
    fun getOpenedCollection(idMarket: String): TaxCollectionSE

}*/
