package com.example.onlineshop.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.onlineshop.ui.fragments.ProductPagingAdapter
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentHome : Fragment(R.layout.fragment_home) {
    private val viewModel: ViewModelHome by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private lateinit var productAdapter: ProductPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        init()
        observe()
    }

    private fun init() = with(binding) {
        productAdapter = ProductPagingAdapter()
        homeList.apply {
            adapter = productAdapter
        }
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.getMostRatedProduct().collectLatest {
                productAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.homeList.adapter = null
        _binding = null
    }
}