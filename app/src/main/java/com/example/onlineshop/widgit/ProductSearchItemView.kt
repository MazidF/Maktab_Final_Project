package com.example.onlineshop.widgit

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.onlineshop.R
import com.example.onlineshop.data.model.ProductSearchItem
import com.example.onlineshop.databinding.ProductSearchItemBinding
import com.example.onlineshop.utils.loadImageInto
import com.google.android.material.card.MaterialCardView

class ProductSearchItemView @JvmOverloads constructor(
    context: Context,
) : LinearLayout(context), Bindable<ProductSearchItem> {
    private val binding: ProductSearchItemBinding

    init {
        val view = inflate(context, R.layout.product_search_item, this)
        binding = ProductSearchItemBinding.bind(view)
    }

    override fun bind(t: ProductSearchItem?): Unit = with(binding) {
        t?.let { item ->
            val product = item.product
            loadImageInto(product.imageUrl, productSearchImage)
            productSearchName.text = product.name
            productSearchPrice.text = product.price
            productSearchRate.text = item.averageRating
            productSearchRealPrice.text = product.regularPrice
            productSearchDiscount.discountPercentHolder.isVisible = product.isOnSale()
        }
    }

    override fun getView(): View {
        return this
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.productSearchRoot.setOnClickListener(l)
    }
}