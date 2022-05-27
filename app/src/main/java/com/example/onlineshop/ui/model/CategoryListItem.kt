package com.example.onlineshop.ui.model

import com.example.onlineshop.R
import com.example.onlineshop.data.model.Category

sealed class CategoryListItem(
    val category: Category
) {
    class Item(
        category: Category
    ) : CategoryListItem(category)

    class ComingSoon : CategoryListItem(
        Category(
            id = 0,
            parent = -1,
            name = "به زودی",
            imageUrl = R.drawable.coming_soon_category,
            count = 0,
        )
    )
}