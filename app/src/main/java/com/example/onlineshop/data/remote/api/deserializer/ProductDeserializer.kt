package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.Product
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ProductDeserializer : JsonDeserializer<Product> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): Product = with(json.asJsonObject) {
        Product(
            id = this["id"].asLong,
            name = this["name"].asString,
            price = this["price"].asString,
            regularPrice = this["regular_price"].asString.apply {
                val a = this
            },
            imageUrl = this["images"].asJsonArray.get(0)
                .asJsonObject["src"].asString
        )
    }
}