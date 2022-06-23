package com.example.onlineshop.data.model.order

enum class OrderStatus(
    val value: String,
) {
    ANY("any"),
    FAILED("failed"),
    PENDING("pending"),
    UNKNOWN("unknown"),
    COMPLETED("completed");

    companion object {
        fun get(status: String): OrderStatus {
            return values().firstOrNull {
                it.value == status
            } ?: UNKNOWN
        }
    }

    override fun toString(): String {
        return value
    }
}