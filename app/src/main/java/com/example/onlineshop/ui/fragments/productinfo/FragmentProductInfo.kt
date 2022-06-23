package com.example.onlineshop.ui.fragments.productinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.example.onlineshop.utils.setHtmlText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: handle rotation

@AndroidEntryPoint
class FragmentProductInfo : Fragment(R.layout.fragment_product_info) {
    private val navController by lazy {
        findNavController()
    }
    private val args: FragmentProductInfoArgs by navArgs()

    private var _binding: FragmentProductInfoBinding? = null
    private val binding: FragmentProductInfoBinding
        get() = _binding!!

    private val cartViewModel: ViewModelCart by activityViewModels()
    private val viewModel: ViewModelProductInfo by viewModels()
    private lateinit var info: ProductInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductInfoBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        if (::info.isInitialized.not()) {
            viewModel.loadProductInfo(args.product)
        }
        productInfoClose.setOnClickListener {
            requireActivity().onBackPressed()
        }
        productInfoOption.setOnClickListener {
            // TODO: do something :)
        }
        productInfoSimilar.apply {
            setOnItemClick {
                onClick(it)
            }
        }
        productInfoAddToCart.apply {
            val productId = args.product.id
            setOnCountChangeListener {
                cartViewModel.updateCart(productId, it)
            }
            this.setupCount(cartViewModel.getProductCount(productId))
        }
    }

    private fun onClick(product: Product) {
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
                        errorDialog(it.error())
                    }
                    is Resource.Loading, is Resource.Reloading -> {
                        // do nothing
                    }
                    is Resource.Success -> {
                        info = it.body()
                        setup()
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
    private fun setup() = with(binding) {
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
        lifecycleScope.launch {
            viewModel.similarStateFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        productInfoSimilar.refresh(it.body())
                    }
                    else -> {
                        // do nothing
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

    private fun errorDialog(error: Throwable) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}