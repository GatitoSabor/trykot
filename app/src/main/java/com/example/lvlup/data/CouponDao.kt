package com.example.lvlup.data

import androidx.room.*

@Dao
interface CouponDao {
    @Insert
    suspend fun insert(coupon: CouponEntity)

    @Query("SELECT * FROM coupons WHERE userId = :userId AND isUsed = 0")
    suspend fun getUnusedCoupons(userId: Int): List<CouponEntity>

    @Update
    suspend fun update(coupon: CouponEntity)
}
