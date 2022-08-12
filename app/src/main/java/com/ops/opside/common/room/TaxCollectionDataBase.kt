package com.ops.opside.common.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ops.opside.common.room.daos.RecollectionDao
import com.ops.opside.common.entities.room.EmailSentRE
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.entities.share.*

@Database(
    entities = [
        TaxCollectionSE::class,
        ConcessionaireSE::class,
        IncidentSE::class,
        ResourcesSE::class,
        TianguisSE::class,
        EmailSentRE::class,
        ParticipatingConcessRE::class], version = 1
)
abstract class TaxCollectionDataBase : RoomDatabase() {
    abstract fun taxCollectionDao(): RecollectionDao
}
