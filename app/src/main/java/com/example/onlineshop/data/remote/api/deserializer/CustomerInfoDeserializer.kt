package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.customer.CustomerInfo
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object CustomerInfoDeserializer : JsonDeserializer<CustomerInfo> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): CustomerInfo = with(json.asJsonObject) {
        CustomerInfo(
            location = this["meta_data"].asJsonArray.map { it.asJsonObject }.firstOrNull {
                it["key"].asString == CustomerInfo.LOCATION_KEY
            }?.let {
                it["value"].asString
            } ?: ""
        )
    }
}