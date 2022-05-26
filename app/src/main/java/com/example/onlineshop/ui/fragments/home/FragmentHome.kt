package com.example.onlineshop.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.databinding.FragmentHomeBinding
import com.example.onlineshop.ui.fragments.adapter.RefreshableAdapter
import com.example.onlineshop.utils.result.SafeApiCall
import com.example.onlineshop.widgit.HorizontalProductContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentHome : Fragment(R.layout.fragment_home) {
    private val navController by lazy {
        findNavController()
    }
    private val viewModel: ViewModelHome by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private lateinit var refreshableAdapter: RefreshableAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        init()
    }

    private fun init() = with(binding) {
        root.setOnRefreshListener {
            loadData()
        }
        refreshableAdapter = createRefreshableAdapter()
        homeList.apply {
            adapter = refreshableAdapter
        }
        if (viewModel.hasBeenLoaded) {
            refresh()
        } else {
            loadData()
        }
    }

    private fun loadData() = with(binding) {
        root.isRefreshing = true
        lifecycleScope.launch {
            val wasSuccessful = viewModel.loadDataAsync().await()
            if (wasSuccessful) {
                root.isRefreshing = false
                refresh()
            } else {
                errorDialog()
            }
        }
    }

    private fun errorDialog() {
        TODO("Not yet implemented")
    }

    private fun refresh() {
        refreshableAdapter.refreshAll()
    }

    private fun createRefreshableAdapter(): RefreshableAdapter = with(viewModel) {
        return RefreshableAdapter(
            list = listOf(
                HorizontalProductContainer(
                    requireContext(),
                    R.drawable.new_offer,
                    "جدید ترین ها:"
                ).apply {
                    setOnItemClick {
                        onClick(it)
                    }
                }, // newest
                HorizontalProductContainer(
                    requireContext(),
                    R.drawable.special_offer
                ).apply {
                    changeBackgroundColor(requireContext().getColor(R.color.discount_red))
                    setOnItemClick {
                        onClick(it)
                    }
                }, // most popular
                HorizontalProductContainer(
                    requireContext(),
                    R.drawable.special_offer,
                    "بهترین ها:"
                ).apply {
                    setOnItemClick {
                        onClick(it)
                    }
                } // most rated,
            ),
            producerList = listOf(
                {
                    (newestProductListFlowState.value as SafeApiCall.Success).body()
                },
                {
                    (mostPopularProductListFlowState.value as SafeApiCall.Success).body()
                },
                {
                    (mostRatedProductListFlowState.value as SafeApiCall.Success).body()
                },
            )
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
        super.onDestroyView()
        binding.homeList.adapter = null
        _binding = null
    }
}