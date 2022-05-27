package com.example.onlineshop.ui.fragments.adapter

import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.example.onlineshop.ui.fragments.adapter.diff_callback.ProductItemDiffItemCallback
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.widgit.Bindable

abstract class ProductPagingAdapter :
    PagingDataAdapter<ProductListItem.Item, ProductPagingAdapter.ProductHolder>(
        ProductItemDiffItemCallback()
    ) {

    inner class ProductHolder(
        bindable: Bindable<ProductListItem.Item>
    ) : BindableHolder<ProductListItem.Item>(bindable)

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract fun onCreateBindable(parent: ViewGroup, viewType: Int): Bindable<ProductListItem.Item>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(onCreateBindable(parent, viewType))
    }
}
