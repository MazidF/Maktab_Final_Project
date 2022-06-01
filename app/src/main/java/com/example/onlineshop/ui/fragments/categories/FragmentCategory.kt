package com.example.onlineshop.ui.fragments.categories

import android.graphics.Color.WHITE
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.databinding.FragmentCategoryBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.fragments.adapter.RefreshableAdapter
import com.example.onlineshop.ui.fragments.cart.FragmentCartDirections
import com.example.onlineshop.ui.model.CategoryListItem
import com.example.onlineshop.ui.model.ProductList
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.utils.result.SafeApiCall
import com.example.onlineshop.widgit.HorizontalCategoryContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentCategory : FragmentConnectionObserver(R.layout.fragment_category) {
    private var _binding: FragmentCategoryBinding? = null
    private val binding: FragmentCategoryBinding
        get() = _binding!!

    private val viewModel: ViewModelCategory by activityViewModels()
    private lateinit var refreshableAdapter: RefreshableAdapter<CategoryListItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryBinding.bind(view)

        observe()
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.categoriesStateFlow.collectLatest {
                when (it) {
                    is SafeApiCall.Fail -> {
                        errorDialog(it.error())
                    }
                    is SafeApiCall.Success -> {
                        onSuccess()
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private fun onSuccess() {
        refreshableAdapter = createRefreshableAdapter()
        binding.categoryList.adapter = refreshableAdapter
        refreshableAdapter.refreshAll()
        endLoading()
    }

    private fun errorDialog(error: Throwable): Unit = with(binding) {
        endLoading()
    }

    private fun createRefreshableAdapter(): RefreshableAdapter<CategoryListItem> = with(viewModel) {
        val list = viewModel.categoriesStateFlow.value.asSuccess()!!.body()
        val categoryMap = viewModel.categoryMap
        val context = requireContext()
        return RefreshableAdapter(
            list = List(list.size) {
                HorizontalCategoryContainer(context, list[it].category.name).apply {
                    setOnItemClick(this@FragmentCategory::onItemClick)
                    setBackgroundColor(WHITE)
                }
            },
            producerList = List(list.size) {
                {
                    categoryMap[list[it].category.id] ?: listOf()
                }
            }
        )
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

    private fun endLoading() = with(binding) {
        categoryLattie.isVisible = false
        categoryList.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.categoryList.adapter = null
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(FragmentCategoryDirections.actionFragmentCategoryToFragmentNetworkConnectionFailed())
    }
}