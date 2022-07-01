package com.example.onlineshop.data.model.order

import com.example.onlineshop.data.model.Coupon
import com.google.gson.annotations.SerializedName

data class SimpleOrder(
    val id: Long,
    @SerializedName("customer_id")
    val customerId: Long,
    val status: OrderStatus,
    @SerializedName("line_items")
    val lineItems: ArrayList<SimpleLineItem>,
    @SerializedName("coupon_lines")
    val coupons: Array<Coupon>,
    @SerializedName("customer_note")
    val note: String
) {

    operator fun plus(item: SimpleLineItem) = apply {
        val index = lineItems.indexOf(item)
        if (index != -1) {
            lineItems.removeAt(index)
        }
        if (item.count >= 0) {
            lineItems.add(item)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleOrder

        if (id != other.id) return false
        if (customerId != other.customerId) return false
        if (status != other.status) return false
        if (lineItems != other.lineItems) return false
        if (!coupons.contentEquals(other.coupons)) return false
        if (note != other.note) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + customerId.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + lineItems.hashCode()
        result = 31 * result + coupons.contentHashCode()
        result = 31 * result + note.hashCode()
        return result
    }
}