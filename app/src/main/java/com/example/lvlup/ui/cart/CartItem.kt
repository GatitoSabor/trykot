package com.example.lvlup.ui.cart

import com.example.lvlup.data.ProductEntity

data class CartItem(
    val producto: ProductEntity,
    var cantidad: Int
)