package com.example.onlineshop.ui.fragments.adapter.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.ui.model.CategoryListItem

class CategoryListItemDiffItemCallback : DiffUtil.ItemCallback<CategoryListItem>() {
    override fun areItemsTheSame(oldItem: CategoryListItem, newItem: CategoryListItem): Boolean {
        return oldItem.category.id == newItem.category.id
    }

    override fun areContentsTheSame(oldItem: CategoryListItem, newItem: CategoryListItem): Boolean {
        return oldItem.category == newItem.category
    }
}