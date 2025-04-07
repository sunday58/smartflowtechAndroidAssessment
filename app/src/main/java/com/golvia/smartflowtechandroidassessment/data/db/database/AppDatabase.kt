package com.golvia.smartflowtechandroidassessment.data.db.database

/**
 * davidsunday
 */

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import com.golvia.smartflowtechandroidassessment.data.db.typeConverter.StringListConverter

@Database(entities = [InventoryResponseItem::class], version = 1)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
}