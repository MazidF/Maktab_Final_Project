package com.example.onlineshop.ui.fragments.newproducts

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentNewProductsBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.model.ProductList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentNewProducts : FragmentConnectionObserver(R.layout.fragment_new_products) {

    private val args: FragmentNewProductsArgs by navArgs()

    private var _binding: FragmentNewProductsBinding? = null
    private val binding: FragmentNewProductsBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewProductsBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {

    }

    private fun observe() = with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {

    }

}
