package com.example.onlineshop.widgit

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.onlineshop.R
import com.example.onlineshop.databinding.ProductCartItemBinding
import com.example.onlineshop.ui.model.ProductCartItem
import com.example.onlineshop.utils.loadImageInto
import com.google.android.material.card.MaterialCardView

class ProductCartItemView @JvmOverloads constructor(
    context: Context,
) : LinearLayout(context), Bindable<ProductCartItem> {
    private val binding: ProductCartItemBinding

    private var onCountChangeListener: ((Int) -> Unit)? = null

    init {
        val view = inflate(context, R.layout.product_cart_item, this)
        binding = ProductCartItemBinding.bind(view)
        binding.productCartCounter.setOnCountChangeListener {
            onCountChangeListener?.invoke(it)
        }
    }

    override fun bind(t: ProductCartItem?): Unit = with(binding) {
        t?.let { item ->
            val product = item.product
            productCartName.text = product.name
            productCartPrice.text = product.price
            loadImageInto(product.imageUrl, productCartImage)
            productCartCounter.setupCount(t.count)
        }
    }

    override fun getView(): View {
        return this
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.productCartRoot.setOnClickListener(l)
        binding.productCartImage.setOnClickListener(l)
    }

    fun setOnCountChangedListener(block: ((Int) -> Unit)?) {
        this.onCountChangeListener = block
    }
}