package com.example.onlineshop.data.model

data class ProductReview(
    val id: Long,
    val date: String,
    val productId: Long, // not necessary
    val reviewer: String,
    val review: String,
    val rating: Int,
    val verified: Boolean,
)