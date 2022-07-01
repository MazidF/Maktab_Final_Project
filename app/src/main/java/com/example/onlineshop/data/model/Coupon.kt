package com.example.onlineshop.data.model

import java.io.Serializable

data class Coupon(
    val code: String,
    val amount: String,
    val type: DiscountType,
    val description: String,
    val possibleEmails: List<String>,
) : Serializable
