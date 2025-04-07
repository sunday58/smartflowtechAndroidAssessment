package com.golvia.smartflowtechandroidassessment.data.db.database

/**
 * davidsunday
 */

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem

@Dao
interface InventoryDao {

    @Query("SELECT * FROM inventory_items ORDER BY id DESC")
    fun getAllItems(): List<InventoryResponseItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InventoryResponseItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<InventoryResponseItem>)

    @Query("DELETE FROM inventory_items WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM inventory_items")
    suspend fun clearAll()

    @Query("SELECT * FROM inventory_items WHERE title LIKE '%' || :query || '%'")
    fun searchItemsByName(query: String): List<InventoryResponseItem>

    @Query("SELECT * FROM inventory_items WHERE id = :itemId")
    fun getItemById(itemId: String): InventoryResponseItem
}