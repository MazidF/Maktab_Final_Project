package com.example.onlineshop.data.model

enum class ProductOrders(val query: String) {
    DATE(query = "date"),
    PRICE(query = "price"),
    RATING(query = "rating"),
    POPULARITY(query = "popularity");

    override fun toString() = query
}