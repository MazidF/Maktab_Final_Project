package com.example.onlineshop.data.model.order

import com.google.gson.annotations.SerializedName


data class Order(
    val id: Long,
    @SerializedName("customer_id")
    val customerId: Long,
    val total: String,
    @SerializedName("total_tax")
    val totalTax: String,
    val status: OrderStatus,
    @SerializedName("line_items")
    val lineItems: List<LineItem>,
    @SerializedName("date_modified_gmt")
    val date: String,
) {

    fun changeLineItemCount(lineItem: LineItem, count: Int): Order {
        val newList: List<LineItem> = lineItems.run {
            val index = indexOfFirst { item ->
                item.productId == lineItem.productId
            }
            if (index == -1) {
                if (count > 0) {
                    this + listOf(lineItem.clone(count))
                } else {
                    this
                }
            } else {
                ArrayList(this).apply {
                    removeAt(index)
                    if (count > 0) {
                        add(lineItem.clone(count))
                    }
                }
            }
        }

        return Order(
            id = id,
            date = date,
            total = total,
            status = status,
            lineItems = newList,
            totalTax = totalTax,
            customerId = customerId,
        )
    }
}