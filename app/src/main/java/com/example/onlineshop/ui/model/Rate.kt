package com.example.onlineshop.ui.model

import android.content.Context
import com.example.onlineshop.R

enum class Rate(
    val color: Int,
    val text: String,
) {
    UNKNOWN(R.color.black, ""),
    VERY_BAD(R.color.red, "خیلی بد"),
    BAD(R.color.orange, "بد"),
    AVERAGE(R.color.yellow, "معمولی"),
    GOOD(R.color.light_green, "خوب"),
    VERY_GOOD(R.color.green, "خیلی خوب");

    fun getColor(context: Context): Int {
        return context.getColor(color)
    }

    companion object {
        fun get(rate: Int): Rate {
            return values()[rate]
        }
    }
}
