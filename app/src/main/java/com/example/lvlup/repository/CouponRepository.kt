package com.example.lvlup.repository

import com.example.lvlup.data.CouponDao
import com.example.lvlup.data.CouponEntity

class CouponRepository(private val couponDao: CouponDao) {
    suspend fun insertCoupon(coupon: CouponEntity) = couponDao.insert(coupon)
    suspend fun getUnusedCoupons(userId: Int) = couponDao.getUnusedCoupons(userId)
    suspend fun useCoupon(coupon: CouponEntity) {
        couponDao.update(coupon.copy(isUsed = true))
    }
}
