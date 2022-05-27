package com.example.onlineshop.widgit

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.example.onlineshop.R
import com.example.onlineshop.databinding.CategoryItemBinding
import com.example.onlineshop.ui.model.CategoryListItem
import com.example.onlineshop.utils.loadImageInto
import com.google.android.material.card.MaterialCardView

class CategoryItem @JvmOverloads constructor(
    context: Context
) : MaterialCardView(context), Bindable<CategoryListItem.Item> {
    private val binding: CategoryItemBinding

    init {
        val view = inflate(context, R.layout.category_item, this)
        view.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(10, 0, 10, 0)
        }
        binding = CategoryItemBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(t: CategoryListItem.Item?): Unit = with(binding) {
        if (t != null) {
            val category = t.category
            categoryItemName.text = category.name
            categoryItemCount.text = "(${category.count})"
            loadImageInto(category.imageUrl, categoryItemImage)
        }
    }

    override fun getView(): View {
        return this
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.categoryItemRoot.setOnClickListener(l)
    }
}