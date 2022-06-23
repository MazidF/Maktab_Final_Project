package com.example.onlineshop.ui.fragments.productlist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentProductListBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.fragments.adapter.LoadingAdapter
import com.example.onlineshop.ui.fragments.adapter.ProductPagingAdapter
import com.example.onlineshop.ui.fragments.adapter.diff_callback.ProductItemDiffItemCallback
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.widgit.Bindable
import com.example.onlineshop.widgit.SimpleVerticalProductItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentProductList : FragmentConnectionObserver(R.layout.fragment_product_list) {
    private var _binding: FragmentProductListBinding? = null
    private val binding: FragmentProductListBinding
        get() = _binding!!

    private val viewModel: ViewModelProductList by viewModels()
    private val args: FragmentProductListArgs by navArgs()
    private lateinit var productAdapter: ProductPagingAdapter<ProductListItem.Item>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductListBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        productAdapter = object : ProductPagingAdapter<ProductListItem.Item>(
            ProductItemDiffItemCallback(),
            onItemClick = this@FragmentProductList::onItemClick
        ) {
            override fun onCreateBindable(
                parent: ViewGroup,
                viewType: Int
            ): Bindable<ProductListItem.Item> {
                return SimpleVerticalProductItem(parent.context, true)
            }
        }
        productListList.adapter = productAdapter.withLoadStateFooter(LoadingAdapter())
    }

    private fun onItemClick(item: ProductListItem.Item) {
        navController.navigate(
            FragmentProductListDirections.actionFragmentProductListToFragmentProductInfo(
                item.product
            )
        )
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.load(args.productList).collect {
                productAdapter.submitData(it)
            }
        }
        launchOnState(Lifecycle.State.STARTED) {
            productAdapter.loadStateFlow.collect {
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

    private fun handleError() {
        // TODO: handle the error
    }

    private fun startLoading(): Unit = with(binding) {
        productListLottie.apply {
            playAnimation()
            isVisible = true
        }
        productListList.isVisible = false
    }

    private fun stopLoading(): Unit = with(binding) {
        productListLottie.apply {
            pauseAnimation()
            isVisible = false
        }
        productListList.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        println()
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(
            FragmentProductListDirections.actionFragmentProductListToFragmentNetworkConnectionFailed()
        )
    }
}
