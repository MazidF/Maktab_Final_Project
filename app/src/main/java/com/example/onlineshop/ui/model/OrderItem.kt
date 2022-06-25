package com.example.onlineshop.ui.model

import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.data.model.order.OrderStatus

data class OrderItem(
    val id: Long,
    val customerId: Long,
    val total: String,
    val totalTax: String,
    val status: OrderStatus,
    val lineItems: List<LineItemWithImage>,
) {
    companion object {

        fun fromOrder(order: Order, list: List<LineItemWithImage>): OrderItem {
            return OrderItem(
                id = order.id,
                lineItems = list,
                total = order.total,
                status = order.status,
                totalTax = order.totalTax,
                customerId = order.customerId,
            )
        }
    }

    fun toOrder(): Order {
        return Order(
            id = this.id,
            total = this.total,
            status = this.status,
            totalTax = this.totalTax,
            customerId = this.customerId,
            lineItems = lineItems.map {
                it.lineItem
            }
        )
    }
}