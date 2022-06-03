package com.example.onlineshop.widgit

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.onlineshop.R
import com.example.onlineshop.databinding.SliderBinding
import com.example.onlineshop.ui.fragments.product_info.viewpager.FragmentImageViewer
import com.example.onlineshop.ui.fragments.product_info.viewpager.ProductImageViewPagerAdapter

class AutoViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : ConstraintLayout(context, attrs) {
    private val binding: SliderBinding

    init {
        val view = inflate(context, R.layout.slider, this)
        binding = SliderBinding.bind(view)
    }

    fun setAdapter(adapter: ProductImageViewPagerAdapter) = with(binding) {
        sliderViewpager.apply {
            this.adapter = adapter
        }
    }

}