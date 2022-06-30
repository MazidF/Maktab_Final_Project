package com.example.onlineshop.data.model.customer

import com.example.onlineshop.data.model.order.OrderStatus
import com.google.gson.annotations.SerializedName

data class RawOrder(
    @SerializedName("customer_id")
    val customerId: Long,
    val status: String = OrderStatus.ON_HOLD.value,
)
