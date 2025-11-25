package com.example.lvlup.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val brand: String,
    val price: Double,
    val description: String,
    val imageUrl: String? = null,
    val imageResId: Int? = null,
    val discountPercent: Double? = null
)
