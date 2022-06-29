package com.example.onlineshop.widgit

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onlineshop.R
import com.example.onlineshop.data.model.ProductSearchItem
import com.example.onlineshop.databinding.PagingViewBinding
import com.example.onlineshop.ui.fragments.adapter.ItemPagingAdapter
import com.example.onlineshop.ui.fragments.adapter.diff_callback.ProductSearchItemDiffItemCallback
import com.example.onlineshop.utils.launchOnState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

@SuppressLint("CustomViewStyleable")
class ProductSearchPagingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : FrameLayout(context, attrs) {
    private var producerJob: Job? = null
    private val binding: PagingViewBinding

    private var onItemClick: (ProductSearchItem) -> Unit = {}

    private val adapter = object : ItemPagingAdapter<ProductSearchItem>(
        ProductSearchItemDiffItemCallback(),
        onItemClick = this::onItemClick,
    ) {
        override fun onCreateBindable(
            parent: ViewGroup,
            viewType: Int
        ): Bindable<ProductSearchItem> {
            return ProductSearchItemView(context)
        }
    }

    init {
        val view = inflate(context, R.layout.paging_view, this)
        binding = PagingViewBinding.bind(view)
        binding.pagingViewList.apply {
            layoutManager = GridLayoutManager(context, 1)
            this.adapter = this@ProductSearchPagingView.adapter
        }
    }

    private fun onItemClick(item: ProductSearchItem) {
        onItemClick.invoke(item)
    }

    fun setOnItemClickListener(block: (ProductSearchItem) -> Unit) {
        onItemClick = block
    }

    fun setup(fragment: Fragment) {
        fragment.launchOnState(Lifecycle.State.STARTED) {
            adapter.loadStateFlow.collect {
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        stopLoading()
                    }
                    LoadState.Loading -> {
                        startLoading()
                    }
                    is LoadState.Error -> {
                        handleError()
                    }
                }
            }
        }
    }

    fun setProducer(fragment: Fragment, flow: Flow<PagingData<ProductSearchItem>>): Unit = with(binding) {
        producerJob?.cancel()
        producerJob = fragment.launchOnState(Lifecycle.State.STARTED) {
            flow.collect {
                adapter.submitData(it)
            }
        }
    }

    private fun startLoading() {
        binding.pagingViewLottie.isVisible = true
    }

    private fun stopLoading() {
        binding.pagingViewLottie.isVisible = false
    }

    private fun handleError() {

    }
}
