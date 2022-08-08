package com.ops.opside.common.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ops.opside.common.Entities.TaxCollectionEntity
import com.ops.opside.common.daos.RecollectionDao

@Database(entities = [TaxCollectionEntity::class],  version = 1)
abstract class TaxCollectionDataBase: RoomDatabase() {
    abstract fun taxCollectionDao(): RecollectionDao
}
