package com.golvia.smartflowtechandroidassessment.repo

import com.golvia.smartflowtechandroidassessment.data.InventoryResponse
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem

/**
 * davidsunday
 */
interface InventoryRepository {
    suspend fun getInventory(): InventoryResponse?
    suspend fun postInventory(): InventoryResponseItem?
    suspend fun putInventory(id: Int): InventoryResponseItem?
    suspend fun deleteInventory(id: Int): InventoryResponseItem?
}