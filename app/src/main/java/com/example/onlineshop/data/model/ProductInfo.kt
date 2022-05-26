package com.example.onlineshop.data.model

data class ProductInfo(
    val totalSales: Int,
    val ratingCount: Int,
    val description: String,
    val averageRating: String,
    val relatedList: List<Long>,
    val imagesUrl: List<String>,
    val dimensions: List<String>,
    val attributes: List<ProductAttribute>
)
