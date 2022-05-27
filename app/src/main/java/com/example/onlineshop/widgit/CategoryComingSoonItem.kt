package com.example.onlineshop.widgit

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.example.onlineshop.R
import com.example.onlineshop.databinding.CategoryComingSoonItemBinding
import com.example.onlineshop.ui.model.CategoryListItem
import com.example.onlineshop.utils.loadImageInto
import com.google.android.material.card.MaterialCardView

class CategoryComingSoonItem @JvmOverloads constructor(
    context: Context
) : MaterialCardView(context), Bindable<CategoryListItem.ComingSoon> {
    private val binding: CategoryComingSoonItemBinding

    init {
        val view = inflate(context, R.layout.category_coming_soon_item, this)
        binding = CategoryComingSoonItemBinding.bind(view)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(t: CategoryListItem.ComingSoon?): Unit = with(binding) {
        if (t != null) {
            val category = t.category
            categoryComingSoonItemName.text = category.name
            loadImageInto(category.imageUrl, categoryComingSoonItemImage)
        }
    }

    override fun getView(): View {
        return this
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.categoryComingSoonItemRoot.setOnClickListener(l)
    }
}