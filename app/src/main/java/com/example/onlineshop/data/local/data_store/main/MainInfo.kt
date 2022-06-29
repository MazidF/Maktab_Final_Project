package com.example.onlineshop.data.local.data_store.main

data class MainInfo(
    val customerId: Long,
    val hasBeenLoggedIn: Boolean,
) {
    fun hasValidId() = customerId != -1L

    companion object {
        val empty = MainInfo(
            customerId = -1,
            hasBeenLoggedIn = false,
        )
    }
}