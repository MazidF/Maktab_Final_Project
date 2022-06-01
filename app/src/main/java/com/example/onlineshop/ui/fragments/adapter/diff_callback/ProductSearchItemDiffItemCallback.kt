package com.example.onlineshop.ui.fragments.adapter.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.data.model.ProductSearchItem

class ProductSearchItemDiffItemCallback : DiffUtil.ItemCallback<ProductSearchItem>() {
    override fun areItemsTheSame(oldItem: ProductSearchItem, newItem: ProductSearchItem): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: ProductSearchItem, newItem: ProductSearchItem): Boolean {
        return oldItem == newItem
    }
}