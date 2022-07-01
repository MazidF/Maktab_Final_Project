package com.example.onlineshop.utils

import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Coupon
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.order.*
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

fun Order.toSimpleOrder(coupons: Array<Coupon>, note: String = ""): SimpleOrder {
    return SimpleOrder(
        id = id,
        status = status,
        coupons = coupons,
        customerId = customerId,
        lineItems = ArrayList(lineItems.map {
            it.toSimpleLineItem()
        }),
        note = note,
    )
}

fun OrderItem.toSimpleOrder(
    coupons: Array<Coupon>,
    note: String = "",
    status: OrderStatus = this.status
): SimpleOrder {
    return SimpleOrder(
        id = id,
        status = status,
        coupons = coupons,
        customerId = customerId,
        lineItems = ArrayList(lineItems.map {
            it.toSimpleLineItem()
        }),
        note = note,
    )
}