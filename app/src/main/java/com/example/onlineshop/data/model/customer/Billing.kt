package com.example.onlineshop.data.model.customer

data class Billing(
    val address_1: String = "",
    val address_2: String = "",
    val city: String = "TEHRAN",
    val company: String = "",
    val country: String = "IRAN",
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val postcode: String = "",
    val state: String = ""
) {
    companion object {
        val empty = Billing(
            email = "empty@emial.com",
            first_name = "first name",
            last_name = "last name",
            phone = "09123456789",
        )
    }
}