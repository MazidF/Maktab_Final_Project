package com.example.onlineshop.ui.fragments.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.ui.fragments.adapter.diff_callback.CategoryListItemDiffItemCallback
import com.example.onlineshop.ui.model.CategoryListItem
import com.example.onlineshop.utils.setMargin
import com.example.onlineshop.widgit.Bindable
import com.example.onlineshop.widgit.CategoryComingSoonItem
import com.example.onlineshop.widgit.CategoryItem

class CategoryAdapter(
    private val onItemClick: (category: Category) -> Unit,
) : ListAdapter<CategoryListItem, CategoryAdapter.CategoryHolder>(
    CategoryListItemDiffItemCallback()
) {
    inner class CategoryHolder(
        private val bindable: Bindable<CategoryListItem>
    ) : BindableHolder<CategoryListItem>(bindable) {
        private var categoryListItem: CategoryListItem? = null

        init {
            bindable.getView().setOnClickListener {
                categoryListItem?.let { item ->
                    if (item is CategoryListItem.Item) {
                        item.category.let(onItemClick)
                    }
                }
            }
        }

        override fun bind(item: CategoryListItem?) {
            super.bind(item)
            categoryListItem = item
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CategoryListItem.Item -> 100
            is CategoryListItem.ComingSoon -> 110
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val bindable: Bindable<CategoryListItem> = if (viewType == 100) {
            CategoryItem(parent.context) as Bindable<CategoryListItem>
        } else {
            CategoryComingSoonItem(parent.context) as Bindable<CategoryListItem>
        }
        return CategoryHolder(bindable)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun refresh(list: List<CategoryListItem>) {
        submitList(list)
        notifyItemChanged(0)
    }
}