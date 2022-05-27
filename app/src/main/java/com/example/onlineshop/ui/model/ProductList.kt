package com.example.onlineshop.ui.model

import com.example.onlineshop.data.model.Category
import java.io.Serializable

sealed class ProductList : Serializable {
    data class ByCategory(
        val category: Category,
    ) : ProductList()

    object Newest : ProductList()
    object MostPopular : ProductList()
    object MostRated : ProductList()
}