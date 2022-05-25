package com.example.onlineshop.widgit

import android.content.Context
import android.graphics.Color.RED
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import com.example.onlineshop.R
import com.example.onlineshop.databinding.HorizontalProductContainerBinding
import com.example.onlineshop.ui.fragments.ProductAdapter
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.insertFooter
import com.example.onlineshop.utils.insertHeader

class HorizontalProductContainer : ConstraintLayout, Refreshable<ProductListItem> {

    companion object {
        const val REFRESHABLE_ID: Int = 1_000_1
    }

    override val refreshableId = REFRESHABLE_ID

    private var imageId: Int = 0
    private var onMoreButtonClick: () -> Unit = {}

    private lateinit var productAdapter: ProductAdapter
    private val binding: HorizontalProductContainerBinding

    init {
        val view = inflate(context, R.layout.horizontal_product_container, this)
        binding = HorizontalProductContainerBinding.bind(view)
        initView()
    }

    constructor(context: Context, imageId: Int) : super(context) {
        this.imageId = imageId
    }

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
                        SimpleVerticalProductFooter(context)
                    }
                    ProductListItem.Item::class.java -> {
                        SimpleVerticalProductItem(context)
                    }
                    ProductListItem.Header::class.java -> {
                        SimpleVerticalProductHeader(context)
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
        changeBackgroundColor(RED)
    }

    fun changeBackgroundColor(color: Int) {
        this.setBackgroundColor(color)
    }

    override fun refresh(list: List<ProductListItem>) {
        val newList = list.insertFooter(
            ProductListItem.Footer(
                imageId = imageId,
                onMoreButtonClick = onMoreButtonClick
            )
        ).insertHeader(
            ProductListItem.Header(
                onMoreButtonClick = onMoreButtonClick
            )
        )
        productAdapter.submitList(newList) {
            with(productAdapter) {
                binding.horizontalContainerList.scrollToPosition(0)
                notifyItemChanged(0)
            }
        }
    }

    override fun bind(t: List<ProductListItem>?) {
        t?.let {
            refresh(it)
        }
    }

    override fun getView(): View {
        return this
    }

    fun setOnMoreButtonClick(block: () -> Unit) {
        this.onMoreButtonClick = block
    }

}