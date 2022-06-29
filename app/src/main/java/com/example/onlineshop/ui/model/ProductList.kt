package com.example.onlineshop.ui.model

import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import java.io.Serializable

sealed class ProductList : Serializable {
    data class ByCategory(
        val category: Category,
    ) : ProductList()

    object Newest : ProductList()
    object MostPopular : ProductList()
    object MostRated : ProductList()

    data class Custom(
        val products: List<Product>
    )
}