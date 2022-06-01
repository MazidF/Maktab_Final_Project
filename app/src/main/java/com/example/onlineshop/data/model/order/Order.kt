package com.example.onlineshop.data.model.order

import com.example.onlineshop.data.model.customer.Billing
import com.example.onlineshop.data.model.customer.Shipping

data class Order(
    val billing: Billing,
    val line_items: List<LineItem>,
    val payment_method: String,
    val payment_method_title: String,
    val set_paid: Boolean,
    val shipping: Shipping,
    val shipping_lines: List<ShippingLine>
)