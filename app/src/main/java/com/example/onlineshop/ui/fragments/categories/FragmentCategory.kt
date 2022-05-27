package com.example.onlineshop.ui.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.databinding.FragmentCategoryBinding
import com.example.onlineshop.ui.fragments.adapter.CategoryAdapter
import com.example.onlineshop.ui.model.ProductList
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.utils.result.SafeApiCall
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentCategory : Fragment(R.layout.fragment_category) {
    private var _binding: FragmentCategoryBinding? = null
    private val binding: FragmentCategoryBinding
        get() = _binding!!

    private val viewModel: ViewModelCategory by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private val navController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryBinding.bind(view)

        init()
        observe()
    }

    private fun init() = with(binding) {
        categoryRoot.setOnRefreshListener {
            loadData()
        }
        categoryAdapter = CategoryAdapter {
            onItemClick(it)
        }
        categoryList.adapter = categoryAdapter
    }

    private fun loadData() = with(binding) {
        viewModel.loadCategories()
    }

    private fun errorDialog(error: Throwable): Unit = with(binding) {
        endLoading(false)
        TODO("Not yet implemented")
    }

    private fun onItemClick(category: Category) {
        navController.navigate(
            FragmentCategoryDirections.actionFragmentCategoryToFragmentProductList(
                ProductList.ByCategory(
                    category
                )
            )
        )
    }

    private fun observe() = with(binding) {
        var hasBeenLoaded = false
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.categoriesStateFlow.collectLatest {
                when (it) {
                    is SafeApiCall.Fail -> {
                        errorDialog(it.error())
                    }
                    is SafeApiCall.Loading -> {
                        hasBeenLoaded = false
                    }
                    is SafeApiCall.Reloading -> {
                        categoryRoot.isRefreshing = true
                    }
                    is SafeApiCall.Success -> {
                        endLoading(hasBeenLoaded.not())
                        hasBeenLoaded = true
                        categoryAdapter.refresh(it.body())
                    }
                }
            }
        }
    }

    private fun endLoading(firstTime: Boolean) = with(binding) {
        if (firstTime) {
            categoryLattie.isVisible = false
            categoryRoot.isVisible = true
        }
        categoryRoot.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.categoryList.adapter = null
        _binding = null
    }
}