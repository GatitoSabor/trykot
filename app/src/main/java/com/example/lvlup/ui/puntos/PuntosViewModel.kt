package com.example.lvlup.ui.puntos

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlup.data.CouponEntity
import com.example.lvlup.repository.CouponRepository
import kotlinx.coroutines.launch

class PuntosViewModel (
    private val couponRepository: CouponRepository
): ViewModel() {
    var puntos by mutableStateOf(200)
    var coupons = listOf(
        CouponEntity(userId = 0, description = "10% de descuento en toda la tienda", pointsCost = 100, discountPercent = 10),
        CouponEntity(userId = 0, description = "Envío sin costo en tu próxima compra", pointsCost = 50, discountPercent = 0),
        CouponEntity(userId = 0, description = "Descuento de $5.000 en compras sobre $30.000", pointsCost = 150, discountPercent = 0)
    )
    var cart = mutableStateListOf<CouponEntity>()

    fun addToCart(coupon: CouponEntity) { if(!cart.contains(coupon)) cart.add(coupon) }
    fun removeFromCart(coupon: CouponEntity) { cart.remove(coupon) }

    fun canjearCupon(userId: Int) {
        viewModelScope.launch {
            cart.forEach { coupon ->
                couponRepository.insertCoupon(
                    CouponEntity(
                        userId = userId,
                        description = coupon.description,
                        discountPercent = coupon.discountPercent,
                        pointsCost = coupon.pointsCost,
                        isUsed = false
                    )
                )
            }
            cart.clear()
        }
    }


}

