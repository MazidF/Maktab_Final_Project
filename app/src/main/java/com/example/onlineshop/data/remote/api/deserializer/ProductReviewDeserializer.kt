package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.ProductReview
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object ProductReviewDeserializer : JsonDeserializer<ProductReview> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): ProductReview = with(json.asJsonObject) {
        ProductReview(
            id = this["id"].asLong,
            rating = this["rating"].asInt,
            review = this["review"].asString,
            reviewer = this["reviewer"].asString,
            date = this["date_created"].asString,
            verified = this["verified"].asBoolean,
            productId = this["product_id"].asLong,
            email = this["reviewer_email"].asString,
        )
    }
}