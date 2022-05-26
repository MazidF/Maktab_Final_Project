package com.example.onlineshop.data.model

import java.io.Serializable

data class Product(
    val id: Long,
    val name: String,
    val price: String,
    val regularPrice: String,
    val imageUrl: String,
) : Serializable {

    fun isOnSale() = price != regularPrice

    companion object {
        fun fake(id: Long): Product {
            return Product(
                id = id,
                name = "",
                price = "",
                regularPrice = "",
                imageUrl = "",
            )
        }
    }
}
