package com.example.onlineshop.ui.model

import com.example.onlineshop.data.model.Product

data class ProductCartItem(
    val product: Product,
    var count: Int,
    // TODO: use val instead
)