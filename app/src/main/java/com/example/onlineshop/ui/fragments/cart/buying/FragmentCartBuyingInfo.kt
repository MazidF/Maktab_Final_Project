package com.example.onlineshop.ui.fragments.cart.buying

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Coupon
import com.example.onlineshop.data.model.DiscountType
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.databinding.FragmentCartBuyingInfoBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.utils.launchOnState

class FragmentCartBuyingInfo : FragmentConnectionObserver(R.layout.fragment_cart_buying_info) {
    private var _binding: FragmentCartBuyingInfoBinding? = null
    private val binding: FragmentCartBuyingInfoBinding
        get() = _binding!!

    private val viewModel: ViewModelCustomerInfo by viewModels()
    private lateinit var imageAdapter: ImageAdapter

    private var productsCost: Float = 0f
    private var coupon: Coupon? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBuyingInfoBinding.bind(view)

        initView()
        observe()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() = with(binding) {
        imageAdapter = ImageAdapter()
        fragmentCartBuyingProductList.apply {
            adapter = imageAdapter
        }
        val list = viewModel.order.lineItems
        imageAdapter.submitList(list)
        productsCost = list.map { it.product.price.ifEmpty { "0" }.toFloat() }.sum()
        fragmentCartBuyingProductsCost.text = productsCost.toString()
        fragmentCartBuyingProductCounter.text = "${list.size} کالا"
        fragmentCartBuyingProductBottomCounter.text = "${list.size} کالا"
        fragmentCartBuyingDiscountBtn.setOnClickListener {
            if (it.tag != null) return@setOnClickListener
            val code = fragmentCartBuyingDiscountText.text.trim()
            fragmentCartBuyingDiscountText.error = if (code.isNotBlank()) {
                checkCoupon(code.toString())
                null
            } else {
                fragmentCartBuyingDiscountText.requestFocus()
                "Empty!!"
            }
        }
        fragmentCartBuyingDiscountText.doAfterTextChanged {
            fragmentCartBuyingDiscountBtn.apply {
                if (this.tag != null) {
                    setImageResource(R.drawable.ic_add)
                    coupon = null
                    tag = null
                }
            }
        }
        fragmentCartBuyingSaveBtn.setOnClickListener {
            saveOrder()
        }
        updateCost()
    }

    private fun saveOrder() = with(binding) {
        val note = fragmentCartBuyingNote.text.toString().trim()
        startLoading()
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.completeOrder(coupon, note).collect {
                if (it) {
                    navigationToCart()
                } else {
                    stopLoading()
                }
            }
        }
    }

    private fun stopLoading() {
        binding.fragmentCartBuyingSaveBtn.isVisible = true
    }

    private fun startLoading() {
        binding.fragmentCartBuyingSaveBtn.isVisible = false
    }

    private fun navigationToCart() {
        navController.navigate(
            FragmentCartBuyingInfoDirections.actionFragmentCartBuyingInfoToFragmentCart()
        )
    }

    private fun updateCost() = with(binding) {
        val discount = fragmentCartBuyingDiscountCost.text.toString().replace(",", "").toFloat()
        val sendCost = fragmentCartBuyingSendCost.text.toString().replace(",", "").toFloat()
        val total = (productsCost + sendCost - discount).toInt().toString()
        fragmentCartBuyingTotalCost.text = total
        fragmentCartBuyingBottomTotalCost.text = total
    }

    private fun checkCoupon(code: String) = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.getCoupon(code).collect {
                when (it) {
                    is Resource.Fail -> {
                        couponFailed()
                    }
                    is Resource.Loading -> {
                        fragmentCartBuyingDiscountLottie.isVisible = true
                    }
                    is Resource.Success -> {
                        val coupon = it.body() ?: return@collect couponFailed()
                        applyCoupon(coupon)
                    }
                }
            }
        }
    }

    private fun applyCoupon(coupon: Coupon) = with(binding) {
        this@FragmentCartBuyingInfo.coupon = coupon
        fragmentCartBuyingDiscountBtn.apply {
            this.tag = true
            setImageResource(R.drawable.ic_green_check)
        }
        fragmentCartBuyingDiscountBox.isVisible = true

        val discount = if (coupon.type == DiscountType.FIXED_CART) {
            coupon.amount
        } else {
            val percent = coupon.amount.toFloat()
            (productsCost * percent / 100).toString()
        }
        fragmentCartBuyingDiscountCost.text = discount
        fragmentCartBuyingProductsCost.text = (productsCost - discount.toFloat()).toString()
        updateCost()
        stopDiscountLottie()
    }

    private fun couponFailed() = with(binding) {
        coupon = null
        fragmentCartBuyingDiscountBtn.apply {
            this.tag = false
            setImageResource(R.drawable.ic_red_close)
        }
        fragmentCartBuyingDiscountCost.text = "0"
        fragmentCartBuyingDiscountBox.isVisible = false
        fragmentCartBuyingProductsCost.text = productsCost.toString()
        updateCost()
        stopDiscountLottie()
    }

    private fun stopDiscountLottie() {
        binding.fragmentCartBuyingDiscountLottie.isVisible = false
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.infoStateFlow.collect {

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {

    }
}