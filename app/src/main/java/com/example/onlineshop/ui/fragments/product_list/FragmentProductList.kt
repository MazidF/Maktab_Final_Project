package com.example.onlineshop.ui.fragments.product_list

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentProductListBinding
import com.example.onlineshop.ui.fragments.adapter.ProductPagingAdapter
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.widgit.Bindable
import com.example.onlineshop.widgit.SimpleVerticalProductItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentProductList : Fragment(R.layout.fragment_product_list) {
    private var _binding: FragmentProductListBinding? = null
    private val binding: FragmentProductListBinding
        get() = _binding!!

    private val viewModel: ViewModelProductList by viewModels()
    private val args: FragmentProductListArgs by navArgs()
    private lateinit var productAdapter: ProductPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductListBinding.bind(view)

        init()
        observe()
    }

    private fun init() = with(binding) {
        productAdapter = object : ProductPagingAdapter() {
            override fun onCreateBindable(
                parent: ViewGroup,
                viewType: Int
            ): Bindable<ProductListItem.Item> {
                return SimpleVerticalProductItem(parent.context, true)
            }
        }
        productListList.adapter = productAdapter
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.CREATED) {
            viewModel.load(args.productList).collect {
                productAdapter.submitData(it)
//                stopLoading()
            }
        }
    }

    private fun stopLoading() = with(binding) {
        productListLottie.isVisible = false
        productListList.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
