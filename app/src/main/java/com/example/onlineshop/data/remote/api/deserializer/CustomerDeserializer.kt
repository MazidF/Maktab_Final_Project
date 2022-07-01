package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.model.customer.CustomerInfo
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object CustomerDeserializer : JsonDeserializer<Customer> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): Customer = with(json.asJsonObject) {
        val currentId = this["meta_data"].asJsonArray.map { it.asJsonObject }.firstOrNull {
            it["key"].asString == CustomerInfo.CURRENT_ORDER_ID_KEY
        }?.let {
            it["value"].asLong
        }
        Customer(
            id = this["id"].asLong,
            email = this["email"].asString,
            username = this["username"].asString,
            password = this["password"]?.asString ?: this["email"].asString,
            firstName = this["first_name"].asString,
            lastName = this["last_name"].asString,
            currentOrderId = currentId ?: -1
        )
    }
}