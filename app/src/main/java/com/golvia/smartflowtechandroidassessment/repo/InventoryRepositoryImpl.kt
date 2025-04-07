package com.golvia.smartflowtechandroidassessment.repo

import com.golvia.smartflowtechandroidassessment.data.InventoryApiService
import com.golvia.smartflowtechandroidassessment.data.InventoryRequest
import com.golvia.smartflowtechandroidassessment.data.InventoryResponse
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import com.golvia.smartflowtechandroidassessment.data.db.database.InventoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

/**
 * davidsunday
 */
class InventoryRepositoryImpl @Inject constructor(
    private val inventoryApiService: InventoryApiService,
    private val inventoryDao: InventoryDao
) : InventoryRepository {

    override fun getInventory(): Flow<List<InventoryResponseItem>> = flow {
        emit(inventoryDao.getAllItems())

        val response = inventoryApiService.getInventory()
        if (response.isSuccessful) {
            val items = response.body().orEmpty()

            inventoryDao.clearAll()
            inventoryDao.insertAll(items)

            emit(inventoryDao.getAllItems())
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            throw mapError(response.code(), errorBody)
        }
    }

    override suspend fun postInventory(inventoryItem: InventoryRequest): InventoryResponseItem? {
        val response = inventoryApiService.postInventory(inventoryItem)
        if (response.isSuccessful) {
            response.body()?.let {
                inventoryDao.insertItem(it)
            }
            return response.body()
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            throw mapError(response.code(), errorBody)
        }
    }

    override suspend fun putInventory(id: Int, inventoryItem: InventoryRequest): InventoryResponseItem? {
        val response = inventoryApiService.putInventory(id, inventoryItem)
        if (response.isSuccessful) {
            response.body()?.let {
                inventoryDao.insertItem(it)
            }
            return response.body()
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            throw mapError(response.code(), errorBody)
        }
    }

    override suspend fun deleteInventory(id: Int): InventoryResponseItem? {
        val response = inventoryApiService.deleteInventory(id)
        if (response.isSuccessful) {
            inventoryDao.deleteById(id)
            return response.body()
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            throw mapError(response.code(), errorBody)
        }
    }

    override fun searchInventoryByName(query: String): Flow<List<InventoryResponseItem>> {
        return flow {
            val filteredItems = inventoryDao.searchItemsByName(query)
            emit(filteredItems)
        }.flowOn(Dispatchers.IO)
    }

    private fun mapError(code: Int, errorBody: String): Exception {
        return when (code) {
            408 -> TimeoutException(parseErrorMessage(errorBody))
            503 -> NoInternetException(parseErrorMessage(errorBody))
            else -> ServerException(parseErrorMessage(errorBody))
        }
    }

    private fun parseErrorMessage(errorBody: String): String {
        return try {
            val json = JSONObject(errorBody)
            json.optString("error", "Unknown error")
        } catch (e: Exception) {
            errorBody
        }
    }
}

/** Thrown when the request times out (HTTP 408). */
class TimeoutException(message: String) : IOException(message)

/** Thrown when there is no internet connection or a 503 from the interceptor. */
class NoInternetException(message: String) : IOException(message)

/** Thrown for general server-side or unknown I/O errors (e.g., status code 500). */
class ServerException(message: String) : Exception(message)