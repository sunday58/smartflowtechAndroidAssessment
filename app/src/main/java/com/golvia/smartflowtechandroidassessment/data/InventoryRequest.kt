package com.golvia.smartflowtechandroidassessment.data

data class InventoryRequest(
    val categoryId: Int = 1,
    val description: String = String(),
    val images: List<String>? = emptyList(),
    val price: Double = 0.0,
    val title: String? = String()
)