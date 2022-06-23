package com.example.onlineshop.ui.fragments.cart.buying

import android.os.Bundle
import android.view.View
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentCartBuyingInfoBinding
import com.example.onlineshop.ui.fragments.FragmentLoadable

class FragmentCartBuyingInfo : FragmentLoadable(R.layout.fragment_cart_buying_info, false) {
    private var _binding: FragmentCartBuyingInfoBinding? = null
    private val binding: FragmentCartBuyingInfoBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBuyingInfoBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {

    }

    private fun observe() = with(binding) {

    }

    override fun refresh() {
        // do nothing
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}