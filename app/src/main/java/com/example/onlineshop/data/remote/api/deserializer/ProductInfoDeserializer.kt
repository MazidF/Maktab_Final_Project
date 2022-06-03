package com.example.onlineshop.data.remote.api.deserializer

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import com.example.onlineshop.data.model.ProductInfo
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object ProductInfoDeserializer : JsonDeserializer<ProductInfo> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): ProductInfo = with(json.asJsonObject) {
        ProductInfo(
            description = this["description"].asString,
            totalSales = this["total_sales"].asInt,
            ratingCount = this["rating_count"].asInt,
            averageRating = this["average_rating"].asString,
            imagesUrl = this["images"].asJsonArray.map {
                it.asJsonObject["src"].asString
            },
            relatedList = this["related_ids"].asJsonArray.map {
                it.asLong
            },
            dimensions = this["dimensions"].asJsonObject.let {
                listOf(it["length"].asString, it["width"].asString, it["height"].asString, )
            },
            attributes = this["attributes"].asJsonArray.map {
                ProductAttributeDeserializer.deserialize(
                    it, typeOfT, context
                )
            }
        )
    }
}