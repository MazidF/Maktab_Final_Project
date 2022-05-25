package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.ProductInfo
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ProductInfoDeserializer : JsonDeserializer<ProductInfo> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): ProductInfo = with(json.asJsonObject) {
        ProductInfo(
            description = this[""].asString,
            totalSales = this["total_sales"].asInt,
            ratingCount = this["rating_count"].asInt,
            averageRating = this["average_rating"].asString,
            imagesUrl = this["images"].asJsonArray.map {
                it.asJsonObject["src"].asString
            },
            relatedList = this["related_ids"].asJsonArray.map {
                it.asInt
            },
            dimensions = this["dimensions"].asJsonArray.map {
                it.asString
            },
            attributes = this["attributes"].asJsonArray.map {
                ProductAttributeDeserializer().deserialize(
                    it, typeOfT, context
                )
            }
        )
    }
}