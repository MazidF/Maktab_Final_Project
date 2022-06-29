package com.example.onlineshop.ui.model

import android.graphics.Color

enum class Rate(
    private val colorString: String,
    val text: String,
) {
    VERY_BAD("#F31818", "خیلی بد"),
    BAD("#C37522", "بد"),
    AVERAGE("#C3C022", "متوسط"),
    GOOD("#20AA21", "خوب"),
    VERY_GOOD("#077308", "خیلی خوب");

    fun color(): Int {
        return Color.parseColor(colorString)
    }

    companion object {
        fun get(rate: Int): Rate {
            return values()[rate]
        }
    }
}
