package com.example.onlineshop.widgit

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.onlineshop.R
import com.example.onlineshop.databinding.SimpleVerticalProductItemBinding
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.loadImageInto
import com.google.android.material.card.MaterialCardView

class SimpleVerticalProductItem @JvmOverloads constructor(
    context: Context,
    elevation: Boolean = false
) : MaterialCardView(context), Bindable<ProductListItem.Item> {
    private val binding: SimpleVerticalProductItemBinding

    init {
        val view = inflate(context, R.layout.simple_vertical_product_item, this)
        view.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            setMargins(10, 5, 10, 5)
        }
        binding = SimpleVerticalProductItemBinding.bind(view).apply {
            if (elevation) {
                root.apply {
                    cardElevation = 20f
                    useCompatPadding = true
                }
            }
        }
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
                simpleVerticalProductDiscount.discountPercentHolder.isVisible = this
            }
        }
    }

    override fun getView(): View {
        return this
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.simpleVerticalProductRoot.setOnClickListener(l)
    }
}
