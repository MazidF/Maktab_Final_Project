package com.example.onlineshop.ui.fragments.orders.sublist

import android.view.ViewGroup
import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.ui.fragments.adapter.ItemPagingAdapter
import com.example.onlineshop.ui.fragments.adapter.diff_callback.OrderItemDiffItemCallback
import com.example.onlineshop.ui.model.OrderItem
import com.example.onlineshop.widgit.Bindable
import com.example.onlineshop.widgit.OrderItemView

class OrderPagingAdapter(
    onItemClick: (OrderItem) -> Unit,
) : ItemPagingAdapter<OrderItem>(
    diffCallback = OrderItemDiffItemCallback(),
    onItemClick = onItemClick,
) {
    override fun onCreateBindable(parent: ViewGroup, viewType: Int): Bindable<OrderItem> {
        return OrderItemView(parent.context)
    }
}