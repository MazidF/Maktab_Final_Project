package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.ProductAttribute
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ProductAttributeDeserializer : JsonDeserializer<ProductAttribute> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ProductAttribute = with(json.asJsonObject) {
        when(this["name"].asString) {
            "Color" -> {
                ProductAttribute.Color(
                    colors = this["options"].asJsonArray.map {
                        it.asString
                    }
                )
            }
            "Size" -> {
                ProductAttribute.Size(
                    sizes = this["options"].asJsonArray.map {
                        it.asString
                    }
                )
            }
            else -> {
                ProductAttribute.Unknown(
                    name = this["name"].asString,
                    attrs = this["options"].asJsonArray.map {
                        it.asString
                    }
                )
            }
        }
    }

}