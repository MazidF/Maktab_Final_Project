package com.example.onlineshop.ui.fragments.adapter.diff_callback

import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.data.model.order.LineItem
import com.example.onlineshop.ui.model.LineItemWithImage

class LineItemWithImageDiffItemCallback : DiffUtil.ItemCallback<LineItemWithImage>() {
    override fun areItemsTheSame(oldItem: LineItemWithImage, newItem: LineItemWithImage): Boolean {
        return oldItem.lineItem.id == newItem.lineItem.id
    }

    override fun areContentsTheSame(oldItem: LineItemWithImage, newItem: LineItemWithImage): Boolean {
        return oldItem == newItem
    }
}