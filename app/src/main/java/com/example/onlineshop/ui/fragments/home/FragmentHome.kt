package com.example.onlineshop.ui.fragments.home

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.databinding.FragmentHomeBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.fragments.adapter.RefreshableAdapter
import com.example.onlineshop.ui.fragments.productinfo.viewpager.ProductImageViewPagerAdapter
import com.example.onlineshop.ui.fragments.productinfo.viewpager.ZoomOutPageTransformer
import com.example.onlineshop.ui.model.ProductList
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.autoScroll
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.utils.failedDialog
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.widgit.HorizontalProductContainer
import dagger.hilt.android.AndroidEntryPoint

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

        initView()
    }

    private fun initView() = with(binding) {
        homeRoot.setOnRefreshListener {
            loadData()
        }
        if (viewModel.hasBeenLoaded) {
            setupAdapter()
            refresh(
                viewModel.imagesFlowState.value.asSuccess()?.body()?.images ?: listOf(),
                viewModel.getState()
            )
            hideLoading(true)
        } else {
            loadData(true)
        }
    }

    private fun setupAdapter() {
        refreshableAdapter = createRefreshableAdapter()
        binding.homeList.apply {
            adapter = refreshableAdapter
        }
    }

    private fun loadData(isFirstTime: Boolean = false) = with(binding) {
        homeRoot.isRefreshing = true
        launchOnState(Lifecycle.State.STARTED) {
            val wasSuccessful = viewModel.loadDataAsync().await()
            if (wasSuccessful) {
                val list = viewModel.imagesFlowState.value.asSuccess()?.body()?.images
                setupAdapter()
                refresh(list ?: listOf(), viewModel.getState())
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
        errorDialog = failedDialog(this::loadData, this::back).also {
            it.show()
        }
    }

    private fun refresh(images: List<String>, state: List<Parcelable?>?) = with(binding) {
        homeSlider.apply {
            adapter = ProductImageViewPagerAdapter(
                fragment = this@FragmentHome,
                fragments = List(images.size) {
                    FragmentSlideViewer().apply {
                        setImage(images[it])
                    }
                }
            )
            autoScroll(3_000)
            setPageTransformer(ZoomOutPageTransformer())
        }
        refreshableAdapter.refreshAll()
        refreshableAdapter.list.forEachIndexed { i, item ->
            (item as? HorizontalProductContainer)?.restore(state?.get(i))
        }
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
                    (newestProductListFlowState.value as Resource.Success).body()
                },
                {
                    (mostPopularProductListFlowState.value as Resource.Success).body()
                },
                {
                    (mostRatedProductListFlowState.value as Resource.Success).body()
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
        val states = refreshableAdapter.list.map {
            (it as? HorizontalProductContainer)?.saveState()
        }
        viewModel.putState(states)
        super.onDestroyView()
        binding.homeList.adapter = null
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(FragmentHomeDirections.actionFragmentHomeToFragmentNetworkConnectionFailed())
    }
}