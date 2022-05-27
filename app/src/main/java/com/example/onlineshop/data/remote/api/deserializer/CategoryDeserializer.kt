package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.Category
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CategoryDeserializer : JsonDeserializer<Category> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): Category = with(json.asJsonObject) {
        Category(
            id = this["id"].asLong,
            parent = this["parent"].asLong,
            name = this["name"].asString,
            imageUrl = this["image"].asJsonObject
                .asJsonObject["src"].asString,
            count = this["count"].asInt
        )
    }
}