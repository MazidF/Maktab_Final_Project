package com.example.onlineshop.data.local.data_store.main

data class MainInfo(
    val userId: Long,
) {
    fun hasBeenLoggedIn() = userId != -1L

    companion object {
        val empty = MainInfo(
            userId = -1,
        )
    }
}