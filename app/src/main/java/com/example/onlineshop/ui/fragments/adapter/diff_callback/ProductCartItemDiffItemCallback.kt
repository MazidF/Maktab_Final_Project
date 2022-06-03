package com.example.onlineshop.ui.fragments.adapter.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.ui.model.ProductCartItem

class ProductCartItemDiffItemCallback : DiffUtil.ItemCallback<ProductCartItem>() {
    override fun areItemsTheSame(oldItem: ProductCartItem, newItem: ProductCartItem): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: ProductCartItem, newItem: ProductCartItem): Boolean {
        return oldItem == newItem
    }
}