package com.example.onlineshop.ui.fragments.adapter.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.data.model.Category

class CategoryDiffItemCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}

