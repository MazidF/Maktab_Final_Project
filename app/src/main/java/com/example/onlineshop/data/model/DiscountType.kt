package com.example.onlineshop.data.model

enum class DiscountType(
    val value: String,
) {
    PERCENT("percent"),
    FIXED_CART("fixed_cart");

    companion object {
        fun get(type: String): DiscountType {
            return values().firstOrNull {
                it.value == type
            } ?: PERCENT
        }
    }
}