package com.example.onlineshop.ui.fragments.product_info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentProductInfoBinding

class FragmentProductInfo : Fragment(R.layout.fragment_product_info) {
    private var _binding: FragmentProductInfoBinding? = null
    private val binding: FragmentProductInfoBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductInfoBinding.bind(view)

        init()
        observe()
    }

    private fun init() = with(binding) {

    }

    private fun observe() = with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}