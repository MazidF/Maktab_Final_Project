package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.order.LineItem
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object LineItemDeserializer : JsonDeserializer<LineItem> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): LineItem = with(json.asJsonObject) {
        LineItem(
            id = this["id"].asLong,
            name = this["name"].asString,
            total = this["total"].asString,
            price = this["price"].asString,
            count = this["quantity"].asInt,
            totalTax = this["total_tax"].asString,
            productId = this["product_id"].asLong,
        )
    }
}