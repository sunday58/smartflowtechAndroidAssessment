package com.golvia.smartflowtechandroidassessment.repo

import com.golvia.smartflowtechandroidassessment.data.InventoryApiService
import com.golvia.smartflowtechandroidassessment.data.InventoryRequest
import com.golvia.smartflowtechandroidassessment.data.InventoryResponse
import com.golvia.smartflowtechandroidassessment.data.InventoryResponseItem
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

/**
 * davidsunday
 */
class InventoryRepositoryImpl @Inject constructor(
    private val inventoryApiService: InventoryApiService
): InventoryRepository {
    override suspend fun getInventory(): InventoryResponse? {
        val response = inventoryApiService.getInventory()
        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"

            when (response.code()) {
                408 -> throw TimeoutException(parseErrorMessage(errorBody))
                503 -> throw NoInternetException(parseErrorMessage(errorBody))
                else -> throw ServerException(parseErrorMessage(errorBody))
            }
        }
    }

    override suspend fun postInventory(inventoryItem: InventoryRequest): InventoryResponseItem? {
        val response = inventoryApiService.postInventory(inventoryItem)
        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"

            when (response.code()) {
                408 -> throw TimeoutException(parseErrorMessage(errorBody))
                503 -> throw NoInternetException(parseErrorMessage(errorBody))
                else -> throw ServerException(parseErrorMessage(errorBody))
            }
        }
    }

    override suspend fun putInventory(id: Int, inventoryItem: InventoryRequest): InventoryResponseItem? {
        val response = inventoryApiService.putInventory(id, inventoryItem)
        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"

            when (response.code()) {
                408 -> throw TimeoutException(parseErrorMessage(errorBody))
                503 -> throw NoInternetException(parseErrorMessage(errorBody))
                else -> throw ServerException(parseErrorMessage(errorBody))
            }
        }
    }

    override suspend fun deleteInventory(id: Int): InventoryResponseItem? {
        val response = inventoryApiService.deleteInventory(id)
        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"

            when (response.code()) {
                408 -> throw TimeoutException(parseErrorMessage(errorBody))
                503 -> throw NoInternetException(parseErrorMessage(errorBody))
                else -> throw ServerException(parseErrorMessage(errorBody))
            }
        }
    }

    /**
     * Optionally, parse the JSON error message returned by the interceptor, e.g.:
     * { "error": "No Internet Connection" }
     */
    private fun parseErrorMessage(errorBody: String): String {
        return try {
            val json = JSONObject(errorBody)
            json.optString("error", "Unknown error")
        } catch (e: Exception) {
            // Fallback if parsing fails
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