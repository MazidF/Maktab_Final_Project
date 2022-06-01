package com.example.onlineshop.ui.fragments.adapter

import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.ui.fragments.adapter.diff_callback.ProductItemDiffItemCallback
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.widgit.Bindable

abstract class ProductPagingAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val onItemClick: (T) -> Unit,
) :
    PagingDataAdapter<T, ProductPagingAdapter<T>.ProductHolder>(
        diffCallback
    ) {

    inner class ProductHolder(
        bindable: Bindable<T>
    ) : BindableHolder<T>(bindable) {
        private var t: T? = null

        init {
            bindable.getView().setOnClickListener {
                t?.let(onItemClick)
            }
        }

        override fun bind(item: T?) {
            super.bind(item)
            t = item
        }
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract fun onCreateBindable(parent: ViewGroup, viewType: Int): Bindable<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(onCreateBindable(parent, viewType))
    }
}
