package com.example.lvlup.ui.cart

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.lvlup.data.ProductEntity

class CartViewModel : ViewModel() {
    var cartItems = mutableStateListOf<CartItem>()
        private set

    fun addToCart(product: ProductEntity) {
        val index = cartItems.indexOfFirst { it.producto.id == product.id }
        if (index != -1) {
            val existente = cartItems[index]
            cartItems[index] = existente.copy(cantidad = existente.cantidad + 1)
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }

    fun removeFromCart(product: ProductEntity) {
        val index = cartItems.indexOfFirst { it.producto.id == product.id }
        if (index != -1) {
            val existente = cartItems[index]
            val nuevaCantidad = existente.cantidad - 1
            if (nuevaCantidad > 0) {
                cartItems[index] = existente.copy(cantidad = nuevaCantidad)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    val total: Double
        get() = cartItems.sumOf {
            val disc = it.producto.discountPercent ?: 0.0
            val precioFinal = it.producto.price * (1 - (disc / 100))
            precioFinal * it.cantidad
        }
}
