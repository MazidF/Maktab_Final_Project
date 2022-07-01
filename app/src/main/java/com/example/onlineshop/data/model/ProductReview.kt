package com.example.onlineshop.data.model

import com.google.gson.annotations.SerializedName

data class ProductReview(
    val id: Long,
    val date: String,
    @SerializedName("product_id")
    val productId: Long, // not necessary
    val reviewer: String,
    @SerializedName("reviewer_email")
    val email: String,
    val review: String,
    val rating: Int,
    val verified: Boolean,
) {
    companion object {
        fun fake(id: Long): ProductReview {
            return ProductReview(
                id = id,
                date = "",
                productId = -1,
                review = "",
                reviewer = "",
                email = "",
                rating = -1,
                verified = false,
            )
        }
    }
}