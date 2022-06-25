package com.example.onlineshop.data.model.order

import com.google.gson.annotations.SerializedName

enum class OrderStatus(
    val value: String,
) {
    ANY("any"),
    FAILED("failed"),
    UNKNOWN("unknown"),
    PENDING("pending"),
    PROCESSING("processing"),
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