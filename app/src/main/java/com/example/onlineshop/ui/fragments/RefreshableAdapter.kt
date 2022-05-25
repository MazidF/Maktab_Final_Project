package com.example.onlineshop.ui.fragments

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.widgit.Refreshable

class RefreshableAdapter(
    private val list: List<Refreshable<ProductListItem>>,
    private val producerList: List<() -> List<ProductListItem>>
) : RecyclerView.Adapter<RefreshableAdapter.RefreshableHolder>() {

    inner class RefreshableHolder(
        private val refreshable: Refreshable<ProductListItem>
    ) : RecyclerView.ViewHolder(
        refreshable.getView()
    ) {
        fun bind(item: List<ProductListItem>) {
            refreshable.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefreshableHolder {
        return RefreshableHolder(list[viewType])
    }

    override fun onBindViewHolder(holder: RefreshableHolder, position: Int) {
//        holder.bind(producerList[position].invoke())
    }

    override fun getItemCount() = list.size

    fun refreshAll() {
        list.forEachIndexed { index, refreshable ->
            refreshable.refresh(producerList[index]())
        }
    }
}