package com.ops.opside.di.providers

import android.content.Context
import androidx.room.Room
import com.ops.opside.common.room.DB_NAME
import com.ops.opside.common.room.TaxCollectionDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): TaxCollectionDataBase {
        return Room.databaseBuilder(
            context,
            TaxCollectionDataBase::class.java, DB_NAME
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

}