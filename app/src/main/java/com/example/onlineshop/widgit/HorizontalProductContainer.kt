package com.example.onlineshop.widgit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color.WHITE
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.databinding.HorizontalProductContainerBinding
import com.example.onlineshop.ui.fragments.adapter.ProductAdapter
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.insertFooter
import com.example.onlineshop.utils.insertHeader

class HorizontalProductContainer : LinearLayout, Refreshable<ProductListItem> {

    companion object {
        const val REFRESHABLE_ID: Int = 1_000_1
    }

    override val refreshableId = REFRESHABLE_ID
    private var imageId: Int = 0

    private var hasTitle = false
    private var onMoreButtonClick: () -> Unit = {}
    private var onItemClick: (Product) -> Unit = {}

    private lateinit var productAdapter: ProductAdapter
    private val binding: HorizontalProductContainerBinding

    init {
        val view = inflate(context, R.layout.horizontal_product_container, this)
        binding = HorizontalProductContainerBinding.bind(view)
        initView()
    }

    constructor(context: Context, imageId: Int, title: String? = null) : super(context) {
        this.imageId = imageId
        title?.let {
            showTitle(title)
        }
    }

    @SuppressLint("CustomViewStyleable")
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val attributes = context.obtainStyledAttributes(
            attrs, R.styleable.horizontal_product_container
        )
        val title = attributes.getString(R.styleable.horizontal_product_container_title)
        if (title != null) {
            showTitle(title)
            val hideMoreBtn = attributes.getBoolean(
                R.styleable.horizontal_product_container_hideMoreBtn, false
            )
            if (hideMoreBtn) {
                hideMoreButton()
            }
        }
        attributes.recycle()
    }

    private fun initView() = with(binding) {
        productAdapter = object : ProductAdapter({
            onItemClick(it)
        }) {

            override fun getItemViewType(position: Int): Int {
                return if (hasTitle) {
                    ProductListItem.getViewType(ProductListItem.Item::class.java)
                } else {
                    when (position) {
                        0 -> ProductListItem.getViewType(ProductListItem.Footer::class.java)
                        currentList.size - 1 -> ProductListItem.getViewType(ProductListItem.Header::class.java)
                        else -> ProductListItem.getViewType(ProductListItem.Item::class.java)
                    }
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
                        SimpleVerticalProductItem(context, hasTitle)
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
    }

    private fun showTitle(title: String): Unit = with(binding) {
        hasTitle = true
        horizontalContainerToolbar.apply {
            isVisible = true
            setOnClickListener {
                onMoreButtonClick()
            }
        }
        horizontalContainerToolbarTitle.text = title
        changeBackgroundColor(WHITE)
        horizontalContainerList.apply {
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 30)
                setPadding(0, 0, 0, 0)
            }
        }
    }

    fun hideMoreButton() {
        binding.horizontalContainerToolbarMoreBtn.isVisible = false
    }

    fun changeBackgroundColor(color: Int) {
        binding.horizontalContainerList.setBackgroundColor(color)
    }

    fun refresh(list: List<ProductListItem>, addHeaderAndFooter: Boolean) {
        val newList = list.let {
            if (addHeaderAndFooter) {
                it.insertFooter(
                    ProductListItem.Footer(
                        imageId = imageId,
                        onMoreButtonClick = onMoreButtonClick
                    )
                ).insertHeader(
                    ProductListItem.Header(
                        onMoreButtonClick = onMoreButtonClick
                    )
                )
            } else {
                it
            }
        }
        productAdapter.submitList(newList) {
            with(productAdapter) {
                binding.horizontalContainerList.scrollToPosition(0)
                if (hasTitle.not()) {
                    notifyItemChanged(0)
                }
            }
        }
    }

    override fun refresh(list: List<ProductListItem>) {
        refresh(list, hasTitle.not())
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

    fun setOnItemClick(block: (Product) -> Unit) {
        this.onItemClick = block
    }

}