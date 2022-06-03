package com.example.onlineshop.data.model.customer

data class Shipping(
    val address_1: String = "",
    val address_2: String = "",
    val city: String = "TEHRAN",
    val company: String = "",
    val country: String = "IRAN",
    val first_name: String,
    val last_name: String,
    val postcode: String = "",
    val state: String = ""
)