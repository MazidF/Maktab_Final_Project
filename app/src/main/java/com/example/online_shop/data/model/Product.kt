package com.example.online_shop.data.model

data class Product(
    val id: Long,
    val name: String,
    val price: String,
    val regularPrice: String,
    val imageUrl: String,
) {

    fun isOnSale() = price != regularPrice
}
