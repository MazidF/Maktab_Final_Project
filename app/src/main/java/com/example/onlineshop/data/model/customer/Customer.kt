package com.example.onlineshop.data.model.customer

data class Customer(
    val id: Long,
    val billing: Billing,
    val email: String,
    val first_name: String,
    val last_name: String,
    val shipping: Shipping,
    val username: String,
    val isPayingCustomer: Boolean,
    val avatarUrl: String,
)