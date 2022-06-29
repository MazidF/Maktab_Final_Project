package com.example.onlineshop.widgit

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import com.example.onlineshop.R
import com.example.onlineshop.databinding.ImageViewWithCounterBinding

class ImageViewWithCounter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : FrameLayout(
    context, attrs
) {
    private val binding: ImageViewWithCounterBinding

    init {
        val view = inflate(context, R.layout.image_view_with_counter, this)
        binding = ImageViewWithCounterBinding.bind(view)

        val attribute = context.obtainStyledAttributes(attrs, R.styleable.ImageViewWithCounter)

        val size = attribute.getDimensionPixelSize(
            R.styleable.ImageViewWithCounter_counterSize, 30
        )
        val margin = attribute.getDimensionPixelSize(
            R.styleable.ImageViewWithCounter_counterMargin, 5
        )
        setCardSize(size, margin)

        val count = attribute.getInteger(R.styleable.ImageViewWithCounter_count, 0)
        setCount(count)

        val src = attribute.getResourceId(R.styleable.ImageViewWithCounter_src, 0)
        setImageSrc(src)

        attribute.recycle()
    }

    private fun setImageSrc(resourceId: Int) {
        binding.imageWithCounterImg.setImageResource(resourceId)
    }

    private fun setCardSize(size: Int, margin: Int) {
        binding.imageWithCounterCard.apply {
            val params = this.layoutParams
            params.height = size
            params.width = size
        }
        binding.imageWithCounterCount.apply {
            val params = this.layoutParams as LayoutParams
            params.setMargins(margin)
        }
    }

    fun setCount(count: Int) = with(binding) {
        imageWithCounterBase.isVisible = if (count > 0) {
            imageWithCounterCount.text = count.toString()
            true
        } else {
            false
        }
    }
}