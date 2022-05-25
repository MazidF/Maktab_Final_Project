package com.example.onlineshop.ui.fragments

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.widgit.Bindable

abstract class ProductAdapter :
    ListAdapter<ProductListItem, ProductAdapter.ProductHolder>(ProductListItemDiffItemCallback()) {

    inner class ProductHolder(
        bindable: Bindable<ProductListItem>
    ) : BindableHolder<ProductListItem>(bindable)

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract fun onCreateBindable(parent: ViewGroup, viewType: Int): Bindable<out ProductListItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(onCreateBindable(parent, viewType) as Bindable<ProductListItem>)
    }
}