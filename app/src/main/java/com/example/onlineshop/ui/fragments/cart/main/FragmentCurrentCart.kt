package com.example.onlineshop.ui.fragments.cart.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.example.onlineshop.R
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.databinding.FragmentCurrentCartBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.model.LineItemWithImage
import com.example.onlineshop.ui.model.OrderItem
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.utils.toSimpleLineItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class FragmentCurrentCart : FragmentConnectionObserver(R.layout.fragment_current_cart) {
    private var _binding: FragmentCurrentCartBinding? = null
    private val binding: FragmentCurrentCartBinding
        get() = _binding!!

    private val viewModel: ViewModelCart by activityViewModels()
    private lateinit var productAdapter: LineItemAdapter


    @Inject
    lateinit var mainDataStore: MainDataStore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCurrentCartBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() : Unit = with(binding) {
        fragmentCartLogin.root.setOnClickListener {
            navController.navigate(FragmentCartDirections.actionFragmentCartToFragmentProfile())
        }
        productAdapter = LineItemAdapter(
            onItemClick = this@FragmentCurrentCart::onItemClick,
            onCountChanged = { item, count, result ->
                viewModel.updateCart(item.toSimpleLineItem(), count) {
                    result(it)
                }
            }
        )
        fragmentCartList.apply {
            adapter = productAdapter
        }
    }

    private fun onItemClick(item: LineItemWithImage) {
        navController.navigate(
            FragmentCartDirections.actionFragmentCartToFragmentProductInfo(
                item.product
            )
        )
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            mainDataStore.preferences.collectLatest {
                fragmentCartLogin.root.isVisible = it.hasValidId().not()
            }
        }
        onSuccess(viewModel.currentOrder())
//        launchOnState(Lifecycle.State.STARTED) {
//            viewModel.orderStateFlow.collect {
//                when (it) {
//                    is Resource.Loading -> {
//                        fragmentCartLottie.isVisible = true
//                    }
//                    is Resource.Fail -> {
//                        onFail(it.error())
//                    }
//                    is Resource.Success -> {
//                        onSuccess(it.body())
//                    }
//                }
//            }
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun onSuccess(order: OrderItem) = with(binding) {
        val list = order.lineItems
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