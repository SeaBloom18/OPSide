package com.ops.opside.common.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.ops.opside.common.entities.room.EmailSentRE
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.entities.share.ParticipatingConcessSE
import com.ops.opside.common.entities.share.*
import com.ops.opside.common.room.daos.*

@Database(
    entities = [
        TaxCollectionSE::class,
        ConcessionaireSE::class,
        IncidentSE::class,
        ResourcesSE::class,
        MarketSE::class,
        EmailSentRE::class,
        ParticipatingConcessSE::class,
        EventRE::class,
        PendingEmailSE::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)]
    )
abstract class TaxCollectionDataBase : RoomDatabase() {
    abstract fun taxCollectionDao(): TaxCollectionDao
    abstract fun concessionaireDao(): ConcessionaireDao
    abstract fun marketDao(): MarketDao
    abstract fun participatingConcessDao(): ParticipatingConcessDao
    abstract fun eventDao(): EventDao
    abstract fun pendingEmailDao(): PendingEmailDao

}

const val DB_NAME = "ops_db"