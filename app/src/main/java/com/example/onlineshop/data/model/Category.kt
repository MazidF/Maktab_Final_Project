package com.example.onlineshop.data.model

data class Category(
    val id: Long,
    val parent: Long,
    val name: String,
    val imageUrl: Any,
    val count: Int,
)