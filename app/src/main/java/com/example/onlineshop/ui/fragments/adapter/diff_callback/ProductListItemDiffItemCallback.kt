package com.example.onlineshop.ui.fragments.adapter.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.clazz

class ProductListItemDiffItemCallback : DiffUtil.ItemCallback<ProductListItem>() {
    override fun areItemsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
        return oldItem.clazz() == newItem.clazz() && when (oldItem) {
            is ProductListItem.Footer, is ProductListItem.Header -> true
            is ProductListItem.Item -> oldItem.product.id == (newItem as ProductListItem.Item).product.id
        }
    }

    override fun areContentsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
        return when (oldItem) {
            is ProductListItem.Footer, is ProductListItem.Header -> true
            is ProductListItem.Item -> oldItem.product == (newItem as ProductListItem.Item).product
        }
    }
}