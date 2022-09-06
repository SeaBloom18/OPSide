package com.ops.opside.common.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ops.opside.common.entities.room.EmailSentRE
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.entities.share.*
import com.ops.opside.common.room.daos.ConcessionaireDao
import com.ops.opside.common.room.daos.MarketDao
import com.ops.opside.common.room.daos.ParticipatingConcessDao
import com.ops.opside.common.room.daos.taxCollectionDao

@Database(
    entities = [
        TaxCollectionSE::class,
        ConcessionaireSE::class,
        IncidentSE::class,
        ResourcesSE::class,
        MarketSE::class,
        EmailSentRE::class,
        ParticipatingConcessRE::class], version = 1
)
abstract class TaxCollectionDataBase : RoomDatabase() {
    abstract fun taxCollectionDao(): taxCollectionDao
    abstract fun concessionaireDao(): ConcessionaireDao
    abstract fun marketDao(): MarketDao
    abstract fun participatingConcessDao(): ParticipatingConcessDao
}

const val DB_NAME = "ops_db"