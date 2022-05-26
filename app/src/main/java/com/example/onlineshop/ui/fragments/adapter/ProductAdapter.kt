package com.example.onlineshop.ui.fragments.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.ui.fragments.adapter.diff_callback.ProductListItemDiffItemCallback
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.widgit.Bindable

abstract class ProductAdapter(
    private val onItemClick: (Product) -> Unit,
) :
    ListAdapter<ProductListItem, ProductAdapter.ProductHolder>(ProductListItemDiffItemCallback()) {

    inner class ProductHolder(
        bindable: Bindable<ProductListItem>,
    ) : BindableHolder<ProductListItem>(bindable) {
        private var item: ProductListItem? = null

        init {
            val view = bindable.getView()
            view.setOnClickListener {
                val item = this.item
                if (item is ProductListItem.Item) {
                    onItemClick(item.product)
                }
            }
        }

        override fun bind(item: ProductListItem?) {
            super.bind(item)
            this.item = item
        }
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract fun onCreateBindable(parent: ViewGroup, viewType: Int): Bindable<out ProductListItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(onCreateBindable(parent, viewType) as Bindable<ProductListItem>)
    }
}