package com.example.onlineshop.data.remote.api.deserializer

import com.example.onlineshop.data.model.Coupon
import com.example.onlineshop.data.model.DiscountType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object CouponDeserializer : JsonDeserializer<Coupon> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): Coupon = with(json.asJsonObject) {
        Coupon(
            code = this["code"].asString,
            amount = this["amount"].asString,
            description = this["description"].asString,
            type = DiscountType.get(this["discount_type"].asString),
            possibleEmails = this["email_restrictions"].asJsonArray
                .map { it.asString },
        )
    }
}