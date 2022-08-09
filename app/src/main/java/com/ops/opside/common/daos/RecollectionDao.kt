package com.ops.opside.common.daos

import androidx.room.*
import com.ops.opside.common.Entities.TaxCollectionEntity

@Dao
interface RecollectionDao {

    @Query("SELECT * FROM TaxCollectionEntity")
    fun getAllTaxes(): MutableList<TaxCollectionEntity>

    @Query("SELECT * FROM TaxCollectionEntity where idCollection = :idCollection")
    fun getTaxById(idCollection: Long): TaxCollectionEntity

    @Insert
    fun addTaxCollection(taxCollectionEntity: TaxCollectionEntity): Long

    @Update
    fun updateTaxCollection(taxCollectionEntity: TaxCollectionEntity)

    @Delete
    fun deleteTaxCollection(taxCollectionEntity: TaxCollectionEntity)
}
