package com.example.onlineshop.data.model.order

data class FinishedOrder(
    val id: Long,
    val total: String,
    val dataCompleted: String,
    val customerNote: String,
    val lineItems: List<LineItem>,
)