package com.golvia.smartflowtechandroidassessment.repo

import com.golvia.smartflowtechandroidassessment.data.InventoryRequest
import com.golvia.smartflowtechandroidassessment.data.InventoryResponse
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import kotlinx.coroutines.flow.Flow

/**
 * davidsunday
 */
interface InventoryRepository {
    fun getInventory(): Flow<List<InventoryResponseItem>>
    suspend fun postInventory(inventoryItem: InventoryRequest): InventoryResponseItem?
    suspend fun putInventory(id: Int, inventoryItem: InventoryRequest): InventoryResponseItem?
    suspend fun deleteInventory(id: Int): Boolean?
    fun searchInventoryByName(query: String): Flow<List<InventoryResponseItem>>
    fun getDetailInventory(id: String): Flow<InventoryResponseItem>
}