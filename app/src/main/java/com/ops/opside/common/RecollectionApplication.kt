package com.ops.opside.common

import android.app.Application
import com.ops.opside.common.dataBase.TaxCollectionDataBase

class RecollectionApplication: Application() {

    companion object{
        lateinit var dataBase: TaxCollectionDataBase
    }

/*override fun onCreate() {

        super.onCreate()

        //Future Migration
        val MIGRATION_1 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE TaxCollectionDataBase ADD COLUMN [] NOT NULL DEFAULT ''")
            }
        }

        dataBase = Room.databaseBuilder(this, TaxCollectionDataBase::class.java,
            "TaxCollectionEntity").addMigrations(MIGRATION_1).build()

    }*/
//}
