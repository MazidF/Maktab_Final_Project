package com.example.onlineshop.data.model.order

data class LineItem(
    val id: Long,
    val productId: Long,
    val name: String,
    val price: String,
    val count: Int,
    val total: String,
    val totalTax: String,
)