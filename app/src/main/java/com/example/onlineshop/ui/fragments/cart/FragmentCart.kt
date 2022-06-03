package com.example.onlineshop.ui.fragments.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.example.onlineshop.R
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.databinding.FragmentCartBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.model.ProductCartItem
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.utils.result.SafeApiCall
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class FragmentCart : FragmentConnectionObserver(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding!!

    private val viewModel: ViewModelCart by activityViewModels()
    private lateinit var productAdapter: ProductCartAdapter


    @Inject
    lateinit var mainDataStore: MainDataStore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        viewModel.loadProducts()
        fragmentCartLogin.root.setOnClickListener {
            navController.navigate(FragmentCartDirections.actionFragmentCartToFragmentProfile())
        }
        productAdapter = ProductCartAdapter(
            onItemClick = this@FragmentCart::onItemClick,
            onCountChanged = { id, count ->
                viewModel.updateCart(id, count)
            }
        )
        fragmentCartList.apply {
            adapter = productAdapter
        }
    }

    private fun onItemClick(item: Product) {
        navController.navigate(FragmentCartDirections.actionFragmentCartToFragmentProductInfo(item))
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            mainDataStore.preferences.collectLatest {
                fragmentCartLogin.root.isVisible = it.hasBeenLoggedIn().not()
            }
        }
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.productStateFlow.collect {
                when (it) {
                    is SafeApiCall.Loading -> {
                        fragmentCartLottie.isVisible = true
                    }
                    is SafeApiCall.Fail -> {
                        onFail(it.error())
                    }
                    is SafeApiCall.Success -> {
                        onSuccess(it.body())
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onSuccess(list: List<ProductCartItem>) = with(binding) {
        val isEmpty = list.isEmpty()
        fragmentCartRoot.isVisible = isEmpty.not()
        fragmentCartEmptyContainer.isVisible = isEmpty
        if (isEmpty.not()) {
            productAdapter.submitList(list)
            fragmentCartCount.text = "${list.size} کالا"
            val totalCost = list.map {
                it.product.price.ifEmpty {
                    "0"
                }.toInt()
            }.reduce { a, b -> a + b }.toString()
            fragmentCartCost.text = totalCost
            fragmentCartCostBottom.text = totalCost
        }
        fragmentCartLottie.isVisible = false
    }

    private fun onFail(error: Throwable) = with(binding) {
        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
        viewModel.retry()
        // TODO: refactor retry method to retry with user order
        fragmentCartLottie.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(FragmentCartDirections.actionFragmentCartToFragmentNetworkConnectionFailed())
    }
}