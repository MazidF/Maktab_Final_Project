package com.example.onlineshop.data.model.order

import com.google.gson.annotations.SerializedName

data class SimpleOrder(
    val id: Long,
    @SerializedName("customer_id")
    val customerId: Long,
    val status: OrderStatus,
    @SerializedName("line_items")
    val lineItems: ArrayList<SimpleLineItem>,
) {

    operator fun plus(item: SimpleLineItem) = apply {
        val index = lineItems.indexOf(item)
        if (index != -1) {
            lineItems.removeAt(index)
        }
        if (item.count > 0) {
            lineItems.add(item)
        }
    }
}