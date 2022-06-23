package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.data.model.order.OrderStatus
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object OrderDeserializer : JsonDeserializer<Order> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): Order = with(json.asJsonObject) {
        Order(
            id = this["id"].asLong,
            total = this["total"].asString,
            totalTax = this["total_tax"].asString,
            customerId = this["customer_id"].asLong,
            status = OrderStatus.get(this["status"].asString),
            lineItems = this["line_items"].asJsonArray.map {
                LineItemDeserializer.deserialize(
                    it, typeOfT, context
                )
            },
        )
    }
}