package com.example.onlineshop.data.model.customer

data class CustomerInfo(
    val location: String,
) {
    companion object {
        const val LOCATION_KEY = "locationKey"
        const val CURRENT_ORDER_ID_KEY = "currentOrderIdKey"
    }
}