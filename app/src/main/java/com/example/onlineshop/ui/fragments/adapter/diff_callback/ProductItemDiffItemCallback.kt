package com.example.onlineshop.ui.fragments.adapter.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.ui.model.ProductListItem

class ProductItemDiffItemCallback : DiffUtil.ItemCallback<ProductListItem.Item>() {
    override fun areItemsTheSame(oldItem: ProductListItem.Item, newItem: ProductListItem.Item): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: ProductListItem.Item, newItem: ProductListItem.Item): Boolean {
        return oldItem.product == newItem.product
    }
}