package com.example.onlineshop.ui.model

import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.order.LineItem

data class LineItemWithImage (
    val lineItem: LineItem,
    val product: Product,
)