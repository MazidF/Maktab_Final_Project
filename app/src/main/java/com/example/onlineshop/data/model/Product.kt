package com.example.onlineshop.data.model

import com.example.onlineshop.data.model.order.LineItem
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

        fun fromLineItem(lineItem: LineItem): Product {
            return Product(
                id = lineItem.productId,
                name = lineItem.name,
                price = lineItem.price,
                regularPrice = lineItem.price,
                imageUrl = "",
            )
        }
    }
}
