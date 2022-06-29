package com.example.onlineshop.ui.fragments.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.widgit.Refreshable

class RefreshableAdapter<T>(
    val list: List<Refreshable<T>>,
    private val producerList: List<() -> List<T>>
) : RecyclerView.Adapter<RefreshableAdapter<T>.RefreshableHolder>() {

    inner class RefreshableHolder(
        private val refreshable: Refreshable<T>
    ) : RecyclerView.ViewHolder(
        refreshable.getView()
    ) {
        fun bind(item: List<T>) {
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
        // TODO: check and remove it
    }

    override fun getItemCount() = list.size

    fun refreshAll() {
        list.forEachIndexed { index, refreshable ->
            refreshable.refresh(producerList[index]())
        }
    }
}