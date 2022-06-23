package com.example.onlineshop.data.model.order

data class Order(
    val id: Long,
    val customerId: Long,
    val total: String,
    val totalTax: String,
    val status: OrderStatus,
    val lineItems: List<LineItem>,
)