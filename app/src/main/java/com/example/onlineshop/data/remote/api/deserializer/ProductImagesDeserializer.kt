package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.ProductImages
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object ProductImagesDeserializer : JsonDeserializer<ProductImages> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): ProductImages = with(json.asJsonObject) {
        ProductImages(
            images = this["images"].asJsonArray.map {
                it.asJsonObject["src"].asString
            }
        )
    }
}