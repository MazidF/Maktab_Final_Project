package com.example.onlineshop.ui.fragments.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.widgit.Bindable

abstract class ItemPagingAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val onItemClick: (T) -> Unit,
) :
    PagingDataAdapter<T, ItemPagingAdapter<T>.ItemHolder>(
        diffCallback
    ) {

    inner class ItemHolder(
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

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract fun onCreateBindable(parent: ViewGroup, viewType: Int): Bindable<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(onCreateBindable(parent, viewType))
    }
}
