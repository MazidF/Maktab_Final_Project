package com.example.onlineshop.ui.fragments.cart.main

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.onlineshop.data.model.order.LineItem
import com.example.onlineshop.ui.fragments.adapter.BindableHolder
import com.example.onlineshop.ui.fragments.adapter.diff_callback.LineItemWithImageDiffItemCallback
import com.example.onlineshop.ui.model.LineItemWithImage
import com.example.onlineshop.widgit.LineItemView

class LineItemAdapter(
    private val onItemClick: (LineItemWithImage) -> Unit,
    private val onCountChanged: (LineItem, Int, (Int) -> Unit) -> Unit,
) : ListAdapter<LineItemWithImage, LineItemAdapter.LineItemHolder>(
    LineItemWithImageDiffItemCallback()
) {

    inner class LineItemHolder(
        private val bindable: LineItemView,
    ) : BindableHolder<LineItemWithImage>(bindable) {
        private var item: LineItemWithImage? = null

        init {
            val view = bindable.getView()
            view.setOnClickListener {
                item?.let {
                    onItemClick(it)
                }
            }
            bindable.setOnCountChangedListener { count ->
                item?.let {
                    onCountChanged.invoke(it.lineItem, count, bindable::setLoadingResult)
                }
            }
        }

        override fun bind(item: LineItemWithImage?) {
            super.bind(item)
            this.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineItemHolder {
        val bindable = LineItemView(parent.context)
        return LineItemHolder(bindable)
    }

    override fun onBindViewHolder(holder: LineItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}