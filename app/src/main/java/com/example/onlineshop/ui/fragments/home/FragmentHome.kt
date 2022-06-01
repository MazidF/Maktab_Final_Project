package com.example.onlineshop.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.databinding.FragmentHomeBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.fragments.adapter.RefreshableAdapter
import com.example.onlineshop.ui.fragments.cart.FragmentCartDirections
import com.example.onlineshop.ui.model.ProductList
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.result.SafeApiCall
import com.example.onlineshop.widgit.HorizontalProductContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentHome : FragmentConnectionObserver(R.layout.fragment_home) {
    private val viewModel: ViewModelHome by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private lateinit var refreshableAdapter: RefreshableAdapter<ProductListItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        init()
    }

    private fun init() = with(binding) {
        homeRoot.setOnRefreshListener {
            loadData()
        }
        refreshableAdapter = createRefreshableAdapter()
        homeList.apply {
            adapter = refreshableAdapter
        }
        if (viewModel.hasBeenLoaded) {
            refresh()
            hideLoading(true)
        } else {
            loadData(true)
        }
    }

    private fun loadData(isFirstTime: Boolean = false) = with(binding) {
        homeRoot.isRefreshing = true
        viewLifecycleOwner.lifecycleScope.launch {
            val wasSuccessful = viewModel.loadDataAsync().await()
            if (wasSuccessful) {
                refresh()
            } else {
                errorDialog()
            }
            hideLoading(isFirstTime)
        }
    }

    private fun hideLoading(isFirstTime: Boolean) = with(binding) {
        if (isFirstTime) {
            homeLottie.isVisible = false
            homeRoot.isVisible = true
        }
        homeRoot.isRefreshing = false
    }

    private fun errorDialog() {
        // TODO: handle error
    }

    private fun refresh() {
        refreshableAdapter.refreshAll()
    }

    private fun createHorizontalProductContainer(
        title: String? = null,
        imageId: Int = 0,
        onMoreButtonClick: () -> Unit
    ): HorizontalProductContainer {
        return HorizontalProductContainer(
            requireContext(),
            imageId = imageId,
            title = title
        ).apply {
            setOnItemClick {
                onClick(it)
            }
            setOnMoreButtonClick(onMoreButtonClick)
        }
    }

    private fun createRefreshableAdapter(): RefreshableAdapter<ProductListItem> = with(viewModel) {
        return RefreshableAdapter(
            list = listOf(
                createHorizontalProductContainer("جدید ترین ها:") {
                    showProductList(ProductList.Newest)
                }, // newest
                createHorizontalProductContainer(imageId = R.drawable.special_offer) {
                    showProductList(ProductList.MostPopular)
                }.apply {
                    changeBackgroundColor(requireContext().getColor(R.color.discount_red))
                }, // most popular
                createHorizontalProductContainer("بهترین ها:") {
                    showProductList(ProductList.MostRated)
                } // most rated,
            ),
            producerList = listOf(
                {
                    (newestProductListFlowState.value as SafeApiCall.Success).body()
                },
                {
                    (mostPopularProductListFlowState.value as SafeApiCall.Success).body()
                },
                {
                    (mostRatedProductListFlowState.value as SafeApiCall.Success).body()
                },
            )
        )
    }

    private fun showProductList(productList: ProductList) {
        navController.navigate(
            FragmentHomeDirections.actionFragmentHomeToFragmentProductList(productList)
        )
    }

    private fun onClick(product: Product) {
        navController.navigate(
            FragmentHomeDirections.actionFragmentHomeToFragmentProductInfo(
                product
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.homeList.adapter = null
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(FragmentHomeDirections.actionFragmentHomeToFragmentNetworkConnectionFailed())
    }
}