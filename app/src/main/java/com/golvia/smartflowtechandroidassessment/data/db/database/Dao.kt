package com.golvia.smartflowtechandroidassessment.data.db.database

/**
 * davidsunday
 */

import androidx.room.*
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {

    @Query("SELECT * FROM inventory_items")
    fun getAllItems(): List<InventoryResponseItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InventoryResponseItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<InventoryResponseItem>)

    @Query("DELETE FROM inventory_items WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM inventory_items")
    suspend fun clearAll()
}