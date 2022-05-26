package com.example.onlineshop.utils

import com.example.onlineshop.data.model.Product
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.result.SafeApiCall

val transformer = { safeApiCall: SafeApiCall<List<Product>> ->
    safeApiCall.map<List<ProductListItem>> { data ->
        data.map {
            ProductListItem.Item(it)
        }
    }
}