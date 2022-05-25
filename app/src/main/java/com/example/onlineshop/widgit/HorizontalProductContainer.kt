package com.example.onlineshop.widgit

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.onlineshop.R
import com.example.onlineshop.databinding.HorizontalProductContainerBinding
import com.example.onlineshop.ui.fragments.ProductAdapter
import com.example.onlineshop.ui.model.ProductListItem

class HorizontalProductContainer : ConstraintLayout, Refreshable<ProductListItem> {
    private val binding: HorizontalProductContainerBinding
    private lateinit var productAdapter: ProductAdapter

    init {
        val view = inflate(context, R.layout.horizontal_product_container, this)
        binding = HorizontalProductContainerBinding.bind(view)
        initView()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private fun initView() = with(binding) {
        productAdapter = object : ProductAdapter() {

            override fun getItemViewType(position: Int): Int {
                return when (position) {
                    0 -> ProductListItem.getViewType(ProductListItem.Footer::class.java)
                    currentList.size - 1 -> ProductListItem.getViewType(ProductListItem.Header::class.java)
                    else -> ProductListItem.getViewType(ProductListItem.Item::class.java)
                }
            }

            override fun onCreateBindable(
                parent: ViewGroup,
                viewType: Int
            ): Bindable<out ProductListItem> {
                val context = parent.context
                return when (ProductListItem.getTypeView(viewType)) {
                    ProductListItem.Footer::class.java -> {
                        SimpleVerticalProductFooter(context).apply {
                            layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
                        }
                    }
                    ProductListItem.Item::class.java -> {
                        SimpleVerticalProductItem(context)
                    }
                    ProductListItem.Header::class.java -> {
                        SimpleVerticalProductHeader(context).apply {
                            layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
                        }
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }
        }
        horizontalContainerList.apply {
            adapter = productAdapter
        }
    }

    private var provider: () -> List<ProductListItem> = {
        listOf()
    }

    fun setProvider(provider: () -> List<ProductListItem>) {
        this.provider = provider
    }

    fun changeBackgroundColor(color: Int) {
        this.setBackgroundColor(color)
    }

    override fun provider(): List<ProductListItem> {
        return provider.invoke()
    }

    override fun refresh(list: List<ProductListItem>) {
        productAdapter.submitList(list)
    }

}