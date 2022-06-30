package com.example.onlineshop.ui.fragments.adapter.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.data.model.ProductReview

class ProductReviewDiffItemCallback : DiffUtil.ItemCallback<ProductReview>() {
    override fun areItemsTheSame(oldItem: ProductReview, newItem: ProductReview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductReview, newItem: ProductReview): Boolean {
        return oldItem == newItem
    }
}