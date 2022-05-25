package com.example.onlineshop.data.model

sealed class ProductAttribute {
    data class Color(
        val colors: List<String>
    ) : ProductAttribute()

    data class Size(
        val sizes: List<String>
    ) : ProductAttribute()

    data class Unknown(
        val name: String,
        val attrs: List<String>
    ) : ProductAttribute()
}