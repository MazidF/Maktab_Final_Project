package com.example.onlineshop.utils

import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.ui.model.CategoryListItem
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.result.SafeApiCall

val productToProductListItemTransformer = { safeApiCall: SafeApiCall<List<Product>> ->
    safeApiCall.map<List<ProductListItem>> { data ->
        data.map {
            ProductListItem.Item(it)
        }
    }
}

val categoryToCategoryListItemTransformer = { safeApiCall: SafeApiCall<List<Category>> ->
    safeApiCall.map<List<CategoryListItem>> { data ->
        data.map {
            CategoryListItem.Item(it)
        }
    }
}