package com.example.onlineshop.widgit

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.databinding.HorizontalCategoryContainerBinding
import com.example.onlineshop.ui.fragments.adapter.CategoryAdapter
import com.example.onlineshop.ui.model.CategoryListItem
import com.example.onlineshop.utils.insertFooter

class HorizontalCategoryContainer : LinearLayout, Refreshable<CategoryListItem> {

    companion object {
        const val REFRESHABLE_ID: Int = 1_000_2
    }

    override val refreshableId = REFRESHABLE_ID
    private var onItemClick: (Category) -> Unit = {}

    private lateinit var categoryAdapter: CategoryAdapter
    private val binding: HorizontalCategoryContainerBinding

    init {
        val view = inflate(context, R.layout.horizontal_category_container, this)
        binding = HorizontalCategoryContainerBinding.bind(view)
        initView()
    }

    @JvmOverloads
    constructor(context: Context, title: String) : super(context) {
        binding.horizontalContainerToolbarTitle.text = title
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private fun initView() = with(binding) {
        categoryAdapter = CategoryAdapter {
            onItemClick(it)
        }
        horizontalContainerList.apply {
            adapter = categoryAdapter
        }
    }

    fun changeBackgroundColor(color: Int) {
        binding.horizontalContainerList.setBackgroundColor(color)
    }

    override fun refresh(list: List<CategoryListItem>) {
        val newList = list.ifEmpty {
            listOf(CategoryListItem.ComingSoon())
        }
        categoryAdapter.submitList(newList) {
            binding.horizontalContainerList.scrollToPosition(0)
        }
    }

    override fun bind(t: List<CategoryListItem>?) {
        t?.let {
            refresh(it)
        }
    }

    override fun getView(): View {
        return this
    }

    fun setOnItemClick(block: (Category) -> Unit) {
        this.onItemClick = block
    }

}