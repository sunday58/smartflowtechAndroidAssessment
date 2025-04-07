package com.golvia.smartflowtechandroidassessment.data

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

class InventoryResponse : ArrayList<InventoryResponseItem>()

@Entity(tableName = "inventory_items")
@Parcelize
data class InventoryResponseItem(
    @Embedded val category: Category?, // embedded as a flat object
    val creationAt: String?,
    val description: String?,
    @PrimaryKey val id: Int, // non-nullable and primary key
    @TypeConverters(StringListConverter::class)
    val images: List<String>?,
    val price: Int?,
    val slug: String?,
    val title: String?,
    val updatedAt: String?
) : Parcelable

@Parcelize
data class Category(
    val creationAt: String?,
    val id: Int?,
    val image: String?,
    val name: String?,
    val slug: String?,
    val updatedAt: String?
) : Parcelable