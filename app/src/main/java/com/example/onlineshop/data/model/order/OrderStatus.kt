package com.example.onlineshop.data.model.order

import com.google.gson.annotations.SerializedName

enum class OrderStatus(
    val value: String,
) {
    ANY("any"),
    FAILED("failed"),
    UNKNOWN("unknown"),
    PENDING("pending"),
    REFUNDED("refunded"),
    @SerializedName("processing")
    PROCESSING("processing"),
    @SerializedName("on-hold")
    ON_HOLD("on-hold"),
    @SerializedName("completed")
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