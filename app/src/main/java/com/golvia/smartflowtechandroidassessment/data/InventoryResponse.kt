package com.golvia.smartflowtechandroidassessment.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class InventoryResponse : ArrayList<InventoryResponseItem>()

@Parcelize
data class InventoryResponseItem(
    val category: Category?,
    val creationAt: String?,
    val description: String?,
    val id: Int?,
    val images: List<String>?,
    val price: Int?,
    val slug: String?,
    val title: String?,
    val updatedAt: String?
): Parcelable

@Parcelize
data class Category(
    val creationAt: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val slug: String?,
    val updatedAt: String?
): Parcelable