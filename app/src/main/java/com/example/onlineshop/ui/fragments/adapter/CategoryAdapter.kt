package com.example.onlineshop.ui.fragments.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.databinding.CategoryItemBinding
import com.example.onlineshop.ui.fragments.adapter.diff_callback.CategoryDiffItemCallback
import com.example.onlineshop.utils.loadImageInto

// TODO: refactor to paging adapter
class CategoryAdapter(
    private val onItemClick: (category: Category) -> Unit,
) : ListAdapter<Category, CategoryAdapter.CategoryHolder>(
    CategoryDiffItemCallback()
) {

    // TODO: refactor to Bindable Holder
    inner class CategoryHolder(
        private val binding: CategoryItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private var category: Category? = null

        init {
            with(binding) {
                root.setOnClickListener {
                    category?.let(onItemClick)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Category): Unit = with(binding) {
            category = item
            categoryItemName.text = item.name
            categoryItemCount.text = "(${item.count})"
            loadImageInto(item.imageUrl, categoryItemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding = CategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun refresh(list: List<Category>) {
        submitList(list)
        notifyItemChanged(0)
    }
}