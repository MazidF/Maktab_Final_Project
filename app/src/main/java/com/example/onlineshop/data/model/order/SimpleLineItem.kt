package com.example.onlineshop.data.model.order

import com.google.gson.annotations.SerializedName

data class SimpleLineItem(
    val id: Long,
    @SerializedName("product_id")
    val productId: Long,
    @SerializedName("quantity")
    val count: Int,
) {
    override fun equals(other: Any?): Boolean {
        return productId == productId
    }

    override fun hashCode(): Int {
        return productId.hashCode()
    }

    fun copy(count: Int = this.count): SimpleLineItem {
        return SimpleLineItem(
            id = id,
            productId = productId,
            count = count,
        )
    }
}