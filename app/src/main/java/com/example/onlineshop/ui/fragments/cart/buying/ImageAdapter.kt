package com.example.onlineshop.ui.fragments.cart.buying

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.databinding.ImageViewWithCounterBinding
import com.example.onlineshop.ui.fragments.adapter.diff_callback.LineItemWithImageDiffItemCallback
import com.example.onlineshop.ui.model.LineItemWithImage
import com.example.onlineshop.utils.loadImageInto

class ImageAdapter : ListAdapter<LineItemWithImage, ImageAdapter.ImageHolder>(
    LineItemWithImageDiffItemCallback()
) {

    inner class ImageHolder(
        private val binding: ImageViewWithCounterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LineItemWithImage) = with(binding) {
            imageWithCounterCount.text = item.lineItem.count.toString()
            loadImageInto(item.product.imageUrl, imageWithCounterImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding = ImageViewWithCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        ).apply {
            imageWithCounterImg.apply {
                layoutParams = FrameLayout.LayoutParams(400, 400).apply {
                    gravity = Gravity.CENTER
                }
            }
        }
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(getItem(position))
    }
}