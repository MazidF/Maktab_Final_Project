package com.example.onlineshop.widgit

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.onlineshop.R
import com.example.onlineshop.databinding.SimpleVerticalProductFooterBinding
import com.example.onlineshop.databinding.SimpleVerticalProductHeaderBinding
import com.example.onlineshop.databinding.SimpleVerticalProductItemBinding
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.loadImageInto
import com.google.android.material.card.MaterialCardView

class SimpleVerticalProductItem @JvmOverloads constructor(
    context: Context
) : MaterialCardView(context), Bindable<ProductListItem.Item> {
    private val binding: SimpleVerticalProductItemBinding

    init {
        val view = inflate(context, R.layout.simple_vertical_product_item, this)
        view.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            setMargins(10, 5, 10, 5)
        }
        binding = SimpleVerticalProductItemBinding.bind(view)
    }

    override fun bind(t: ProductListItem.Item?): Unit = with(binding) {
        if (t != null) {
            val item = t.product
            loadImageInto(item.imageUrl, simpleVerticalProductImage)
            simpleVerticalProductName.text = item.name
            simpleVerticalProductCost.text = item.price
            with(item.isOnSale()) {
                if (this) {
                    simpleVerticalProductRealCost.text = item.regularPrice
                }
                // TODO: setup the discount percent
                simpleVerticalProductRealCost.isInvisible = this.not()
                simpleVerticalProductDiscountPercentHolder.isVisible = this
            }
        }
    }

    override fun getView(): View {
        return this
    }
}

class SimpleVerticalProductFooter @JvmOverloads constructor(
    context: Context
) : LinearLayout(context), Bindable<ProductListItem.Footer> {
    private val binding: SimpleVerticalProductFooterBinding

    init {
        val view = inflate(context, R.layout.simple_vertical_product_footer, this)
        view.layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
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

class SimpleVerticalProductHeader @JvmOverloads constructor(
    context: Context
) : MaterialCardView(context), Bindable<ProductListItem.Header> {
    private val binding: SimpleVerticalProductHeaderBinding

    init {
        val view = inflate(context, R.layout.simple_vertical_product_header, this)
        view.layoutParams = LayoutParams(WRAP_CONTENT, MATCH_PARENT).apply {
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

