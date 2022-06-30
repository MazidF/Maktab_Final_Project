package com.example.onlineshop.utils

import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.order.LineItem
import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.data.model.order.SimpleLineItem
import com.example.onlineshop.data.model.order.SimpleOrder
import com.example.onlineshop.ui.model.CategoryListItem
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.ui.model.LineItemWithImage
import com.example.onlineshop.ui.model.OrderItem

val productToProductListItemTransformer = { safeApiCall: Resource<List<Product>> ->
    safeApiCall.map<List<ProductListItem>> { data ->
        data.map {
            ProductListItem.Item(it)
        }
    }
}

val categoryToCategoryListItemTransformer = { safeApiCall: Resource<List<Category>> ->
    safeApiCall.map<List<CategoryListItem>> { data ->
        data.map {
            CategoryListItem.Item(it)
        }
    }
}

fun Product.toSimpleLineItem(count: Int): SimpleLineItem {
    return SimpleLineItem(
        id = 0,
        productId = id,
        count = count,
    )
}

fun LineItem.toSimpleLineItem(): SimpleLineItem {
    return SimpleLineItem(
        id = id,
        productId = productId,
        count = count,
    )
}

fun LineItemWithImage.toSimpleLineItem(): SimpleLineItem {
    return lineItem.toSimpleLineItem()
}

fun Order.toSimpleOrder(): SimpleOrder {
    return SimpleOrder(
        id = id,
        customerId = customerId,
        status = status,
        lineItems = ArrayList(lineItems.map {
            it.toSimpleLineItem()
        })
    )
}

fun OrderItem.toSimpleOrder(): SimpleOrder {
    return SimpleOrder(
        id = id,
        customerId = customerId,
        status = status,
        lineItems = ArrayList(lineItems.map {
            it.toSimpleLineItem()
        })
    )
}