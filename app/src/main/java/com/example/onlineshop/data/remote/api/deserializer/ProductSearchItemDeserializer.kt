package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.ProductSearchItem
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object ProductSearchItemDeserializer : JsonDeserializer<ProductSearchItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): ProductSearchItem = with(json.asJsonObject) {
        ProductSearchItem(
            product = ProductDeserializer.deserialize(json, typeOfT, context),
            averageRating = this["average_rating"].asString,
        )
    }
}