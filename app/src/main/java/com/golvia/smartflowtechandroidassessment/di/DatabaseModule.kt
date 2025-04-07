package com.golvia.smartflowtechandroidassessment.di

import android.content.Context
import androidx.room.Room
import com.golvia.smartflowtechandroidassessment.data.db.database.AppDatabase
import com.golvia.smartflowtechandroidassessment.data.db.database.InventoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * davidsunday
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "inventory_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideInventoryDao(database: AppDatabase): InventoryDao {
        return database.inventoryDao()
    }
}