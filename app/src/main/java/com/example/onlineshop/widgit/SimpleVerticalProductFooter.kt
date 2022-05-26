package com.example.onlineshop.widgit

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.onlineshop.R
import com.example.onlineshop.databinding.SimpleVerticalProductFooterBinding
import com.example.onlineshop.ui.model.ProductListItem

class SimpleVerticalProductFooter @JvmOverloads constructor(
    context: Context
) : LinearLayout(context), Bindable<ProductListItem.Footer> {
    private val binding: SimpleVerticalProductFooterBinding

    init {
        val view = inflate(context, R.layout.simple_vertical_product_footer, this)
        view.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(10, 5, 50, 5)
        }
        binding = SimpleVerticalProductFooterBinding.bind(view)
    }

    override fun bind(t: ProductListItem.Footer?) = with(binding) {
        if (t != null) {
            simpleVerticalFooterImage.setImageResource(t.imageId)
            simpleVerticalFooterBtn.setOnClickListener {
                t.onMoreButtonClick()
            }
        }
    }

    override fun getView(): View {
        return this
    }
}