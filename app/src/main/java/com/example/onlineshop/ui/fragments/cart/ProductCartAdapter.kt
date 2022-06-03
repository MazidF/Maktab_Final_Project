package com.example.onlineshop.ui.fragments.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.ui.fragments.adapter.BindableHolder
import com.example.onlineshop.ui.fragments.adapter.diff_callback.ProductCartItemDiffItemCallback
import com.example.onlineshop.ui.model.ProductCartItem
import com.example.onlineshop.widgit.ProductCartItemView

class ProductCartAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onCountChanged: (Long, Int) -> Unit,
) : ListAdapter<ProductCartItem, ProductCartAdapter.ProductCartItemHolder>(
    ProductCartItemDiffItemCallback()
) {

    inner class ProductCartItemHolder(
        private val bindable: ProductCartItemView,
    ) : BindableHolder<ProductCartItem>(bindable) {
        private var item: ProductCartItem? = null

        init {
            val view = bindable.getView()
            view.setOnClickListener {
                item?.let {
                    onItemClick(it.product)
                }
            }
            bindable.setOnCountChangedListener { count ->
                item?.let {
                    it.count = count
                    onCountChanged.invoke(it.product.id, count)
                }
            }
        }

        override fun bind(item: ProductCartItem?) {
            super.bind(item)
            this.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCartItemHolder {
        val bindable = ProductCartItemView(parent.context)
        return ProductCartItemHolder(bindable)
    }

    override fun onBindViewHolder(holder: ProductCartItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}