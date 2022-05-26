package com.example.onlineshop.widgit

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.example.onlineshop.R
import com.example.onlineshop.databinding.SimpleVerticalProductHeaderBinding
import com.example.onlineshop.ui.model.ProductListItem
import com.google.android.material.card.MaterialCardView

class SimpleVerticalProductHeader @JvmOverloads constructor(
    context: Context
) : MaterialCardView(context), Bindable<ProductListItem.Header> {
    private val binding: SimpleVerticalProductHeaderBinding

    init {
        val view = inflate(context, R.layout.simple_vertical_product_header, this)
        view.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).apply {
            setMargins(50, 5, 10, 5)
        }
        binding = SimpleVerticalProductHeaderBinding.bind(view)
    }

    override fun bind(t: ProductListItem.Header?) = with(binding) {
        if (t != null) {
            root.setOnClickListener {
                t.onMoreButtonClick()
            }
        }
    }

    override fun getView(): View {
        return this
    }
}