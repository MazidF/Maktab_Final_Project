package com.example.onlineshop.widgit

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.onlineshop.R
import com.example.onlineshop.databinding.LineItemBinding
import com.example.onlineshop.ui.model.LineItemWithImage
import com.example.onlineshop.utils.loadImageInto

class LineItemView @JvmOverloads constructor(
    context: Context,
) : LinearLayout(context), Bindable<LineItemWithImage> {
    private val binding: LineItemBinding

    private var onCountChangeListener: ((Int) -> Unit)? = null

    init {
        val view = inflate(context, R.layout.line_item, this)
        binding = LineItemBinding.bind(view)
        binding.productCartCounter.setOnCountChangeListener {
            onCountChangeListener?.invoke(it)
        }
    }

    override fun bind(t: LineItemWithImage?): Unit = with(binding) {
        t?.let { item ->
            val lineItem = item.lineItem
            val product = item.product
            productCartName.text = lineItem.name
            productCartPrice.text = lineItem.price
            loadImageInto(product.imageUrl, productCartImage)
            productCartCounter.setupCount(lineItem.count)
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

    fun setLoadingResult(newCount: Int) {
        binding.productCartCounter.setLoadingResult(newCount)
    }
}