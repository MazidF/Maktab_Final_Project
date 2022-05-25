package com.example.onlineshop.ui.fragments.home

import android.graphics.Color.RED
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentHomeBinding
import com.example.onlineshop.ui.fragments.ProductPagingAdapter
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.insertFooter
import com.example.onlineshop.utils.insertHeader
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.widgit.Bindable
import com.example.onlineshop.widgit.SimpleVerticalProductItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentHome : Fragment(R.layout.fragment_home) {
    private val viewModel: ViewModelHome by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private lateinit var productAdapter: ProductPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        init()
        observe()
    }

    private fun init() = with(binding) {
        homeHorizontalList.apply {
            setProvider {
                viewModel.someProductListFlowState.value
            }
            changeBackgroundColor(RED)
        }
        productAdapter = object : ProductPagingAdapter() {
            override fun onCreateBindable(
                parent: ViewGroup,
                viewType: Int
            ): Bindable<ProductListItem.Item> {
                return SimpleVerticalProductItem(parent.context)
            }
        }
        homeList.apply {
            adapter = productAdapter
        }
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.getMostRatedProduct().collectLatest {
                productAdapter.submitData(it)
            }
        }
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.someProductListFlowState.collect {
                if (it.isNotEmpty()) {
                    val list = it.insertFooter(
                        ProductListItem.Footer(
                            imageId = R.drawable.special_offer,
                            onMoreButtonClick = {
                                // TODO: navigate and show all
                            }
                        )
                    ).insertHeader(
                        ProductListItem.Header(
                            onMoreButtonClick = {
                                // TODO: here too
                            }
                        )
                    )
                    homeHorizontalList.refresh(list)
                } else {
                    // TODO: shimmer
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.homeList.adapter = null
        _binding = null
    }
}