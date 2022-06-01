package com.example.onlineshop.data.model.customer

data class RawCustomer(
    val billing: Billing,
    val email: String,
    val first_name: String,
    val last_name: String,
    val shipping: Shipping,
    val username: String
)
