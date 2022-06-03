package com.example.onlineshop.data.model.customer

import com.google.gson.annotations.SerializedName

data class RawCustomer(
//    val billing: Billing,
    val email: String,
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("last_name")
    val lastName: String = "",
//    val shipping: Shipping,
    val username: String = "",
    val password: String,
)
