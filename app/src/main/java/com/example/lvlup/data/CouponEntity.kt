package com.example.lvlup.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coupons")
data class CouponEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val description: String,
    val discountPercent: Int,
    val pointsCost: Int,
    val isUsed: Boolean = false
)
