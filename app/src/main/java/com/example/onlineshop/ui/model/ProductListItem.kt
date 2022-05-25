package com.example.onlineshop.ui.model

import com.example.onlineshop.data.model.Product
import com.example.onlineshop.utils.clazz

sealed class ProductListItem {
    data class Item(
        val product: Product
    ) : ProductListItem()
    data class Footer(
        val imageId: Int,
        val onMoreButtonClick: () -> Unit
    ) : ProductListItem()
    data class Header(
        val onMoreButtonClick: () -> Unit
    ) : ProductListItem()

    companion object {

        fun getViewType(clazz: Class<out ProductListItem>): Int {
            return when(clazz) {
                Footer::class.java -> 1
                Item::class.java -> 2
                Header::class.java -> 3
                else -> {
                    -1
                }
            }
        }

        fun getTypeView(int: Int): Class<out ProductListItem> {
            return when(int) {
                1 -> Footer::class.java
                2 -> Item::class.java
                3 -> Header::class.java
                else -> throw Exception("ProductListItem:[invalid type view!!]")
            }
        }
    }
}
