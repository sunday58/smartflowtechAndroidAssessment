package com.golvia.smartflowtechandroidassessment.data


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * davidsunday
 */
interface InventoryApiService {
    @GET("api/v1/products")
    suspend fun getInventory(): Response<InventoryResponse>

    @GET("api/v1/products/{id}")
    suspend fun getDetailInventory(
        @Path("id") id: String
    ): Response<InventoryResponseItem>

    @POST("api/v1/products")
    suspend fun postInventory(
        @Body inventoryItem: InventoryRequest
    ): Response<InventoryResponseItem>

    @PUT("api/v1/products/{id}")
    suspend fun putInventory(
        @Path("id") id: Int,
        @Body inventoryItem: InventoryRequest
    ): Response<InventoryResponseItem>

    @DELETE("api/v1/products/{id}")
    suspend fun deleteInventory(
        @Path("id") id: Int
    ): Response<InventoryResponseItem>

}