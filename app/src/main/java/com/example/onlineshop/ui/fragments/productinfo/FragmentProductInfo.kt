package com.example.onlineshop.ui.fragments.productinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductInfo
import com.example.onlineshop.databinding.FragmentProductInfoBinding
import com.example.onlineshop.ui.fragments.cart.main.ViewModelCart
import com.example.onlineshop.ui.fragments.productinfo.viewpager.FragmentImageViewer
import com.example.onlineshop.ui.fragments.productinfo.viewpager.ProductImageViewPagerAdapter
import com.example.onlineshop.ui.fragments.productinfo.viewpager.ZoomOutPageTransformer
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.fragments.productinfo.review.ProductReviewAdapter
import com.example.onlineshop.utils.failedDialog
import com.example.onlineshop.utils.setHtmlText
import dagger.hilt.android.AndroidEntryPoint

// TODO: handle rotation

@AndroidEntryPoint
class FragmentProductInfo : FragmentConnectionObserver(R.layout.fragment_product_info) {
    private val args: FragmentProductInfoArgs by navArgs()

    private var _binding: FragmentProductInfoBinding? = null
    private val binding: FragmentProductInfoBinding
        get() = _binding!!

    private val cartViewModel: ViewModelCart by activityViewModels()
    private val viewModel: ViewModelProductInfo by viewModels()
    private lateinit var info: ProductInfo

    private lateinit var reviewAdapter: ProductReviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductInfoBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        val product = args.product
        viewModel.loadProductInfo(product)
        productInfoClose.setOnClickListener {
            back()
        }
        productInfoCart.setOnClickListener {
            navController.navigate(
                FragmentProductInfoDirections.actionFragmentProductInfoToFragmentCart()
            )
        }
        productInfoSimilar.apply {
            setOnItemClick {
                onProductItemClick(it)
            }
        }
        productInfoAddReview.setOnClickListener {
            navController.navigate(
                FragmentProductInfoDirections.actionFragmentProductInfoToFragmentReviewMaker(
                    product.id
                )
            )
        }
        productInfoAddToCart.apply {
            this.setupCount(cartViewModel.getCount(product.id))
            setOnCountChangeListener { count ->
                cartViewModel.updateCart(product.id, count) { newCount ->
                    setLoadingResult(newCount)
                }
            }
        }
        productInfoReviewsBtn.setOnClickListener {
            navController.navigate(
                FragmentProductInfoDirections.actionFragmentProductInfoToFragmentReviewList(
                    product.id
                )
            )
        }
        reviewAdapter = ProductReviewAdapter {}
        binding.productInfoReviews.adapter = reviewAdapter
    }

    private fun onProductItemClick(product: Product) {
        navController.navigate(
            FragmentProductInfoDirections.actionFragmentProductInfoSelf(
                product
            )
        )
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.productInfoStateFlow.collect {
                when (it) {
                    is Resource.Fail -> {
                        errorDialog {
                            viewModel.loadProductInfo(args.product)
                        }
                    }
                    is Resource.Success -> {
                        info = it.body()
                        setup()
                    }
                }
            }
        }
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.reviewsStateFlow.collect {
                if (it is Resource.Success) {
                    val list = it.body()
                    if (list.isEmpty()) {
                        productInfoReviewsBtn.isVisible = false
                        binding.productInfoReviews.isVisible = false
                    } else {
                        reviewAdapter.submitList(list)
                    }
                }
            }
        }
    }

    private fun stopLoading() = with(binding) {
        productInfoLattie.isVisible = false
        productInfoAppbar.isVisible = true
        productInfoScroll.isVisible = true
        productInfoLinear.isVisible = true
        productInfoLattie.pauseAnimation()
    }

    @SuppressLint("SetTextI18n")
    private fun setup(): Unit = with(binding) {
        setupImages(info.imagesUrl)
        with(args.product) {
            productInfoName.text = name
            productInfoPrice.text = price
            productInfoDiscountHolder.isVisible = if (isOnSale()) {
                productInfoReadPrice.text = regularPrice
                true
            } else {
                false
            }
        }
        with(info) {
            productInfoDescription.setHtmlText(description)
            productInfoSalesCount.text = "($totalSales)"
            productInfoRate.text = ratingCount.toString()
        }
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.similarStateFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        productInfoSimilar.refresh(it.body())
                    }
                }
            }
        }
        stopLoading()
    }

    private fun setupImages(images: List<String>) {
        binding.productInfoImages.apply {
            setPageTransformer(ZoomOutPageTransformer())
            adapter = ProductImageViewPagerAdapter(
                fragment = this@FragmentProductInfo,
                fragments = List(images.size) {
                    FragmentImageViewer().apply {
                        setImage(images[it])
                    }
                }
            )
        }
    }

    private fun errorDialog(block: () -> Unit) {
        errorDialog = failedDialog(block, this::back).also {
            it.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.productInfoReviews.adapter = null
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(
            FragmentProductInfoDirections.actionFragmentProductInfoToFragmentNetworkConnectionFailed()
        )
    }
}