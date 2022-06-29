package com.example.onlineshop.ui.fragments

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.ui.fragments.adapter.BindableHolder

class SeparatorAdapter<T : Any>(
    private val separatorResourceId: Int,
    private val bindableMaker: () -> BindableHolder<T>,
    differCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<SeparatorAdapter.ListItem<T>, RecyclerView.ViewHolder>(DiffCallbackT(differCallback)) {

    sealed class ListItem<T> {
        class Item<T>(
            val item: T,
        ) : ListItem<T>()

        class Separator<T>(
            val id: Int,
        ) : ListItem<T>()
    }

    class DiffCallbackT<T : Any>(
        private val differCallback: DiffUtil.ItemCallback<T>
    ) : DiffUtil.ItemCallback<ListItem<T>>() {

        override fun areItemsTheSame(oldItem: ListItem<T>, newItem: ListItem<T>): Boolean {
            return when(oldItem) {
                is ListItem.Item -> {
                    if (newItem is ListItem.Item) {
                        differCallback.areItemsTheSame(oldItem.item, newItem.item)
                    } else {
                        false
                    }
                }
                is ListItem.Separator -> {
                    if (newItem is ListItem.Separator) {
                        oldItem.id == newItem.id
                    } else {
                        false
                    }
                }
            }
        }

        override fun areContentsTheSame(oldItem: ListItem<T>, newItem: ListItem<T>): Boolean {
            return when(oldItem) {
                is ListItem.Item -> {
                    if (newItem is ListItem.Item) {
                        differCallback.areContentsTheSame(oldItem.item, newItem.item)
                    } else {
                        false
                    }
                }
                is ListItem.Separator -> {
                    if (newItem is ListItem.Separator) {
                        oldItem.id == newItem.id
                    } else {
                        false
                    }
                }
            }
        }
    }

    @JvmName("submitListT")
    fun submitList(list: MutableList<T>) {
        if (list.isEmpty()) {
            super.submitList(listOf())
            return
        }
        val newList : List<ListItem<T>> = List(list.size * 2 - 1) {
            if (it % 2 == 0) {
                ListItem.Item(list[it / 2])
            } else {
                ListItem.Separator((it - 1) / 2)
            }
        }
        super.submitList(newList)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType % 2 == 0) {
            bindableMaker()
        } else {
            val view = View.inflate(parent.context, separatorResourceId, null)
            object : RecyclerView.ViewHolder(view) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position % 2 == 0) {
            (holder as BindableHolder<T>).bind((getItem(position) as ListItem.Item).item)
        }
    }

}