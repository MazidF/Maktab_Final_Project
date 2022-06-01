package com.example.onlineshop.data.model.order

data class LineItem(
    val product_id: Int,
    val quantity: Int,
    val variation_id: Int
)