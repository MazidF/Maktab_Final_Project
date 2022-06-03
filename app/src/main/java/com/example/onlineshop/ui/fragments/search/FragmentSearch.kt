package com.example.onlineshop.ui.fragments.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.onlineshop.R
import com.example.onlineshop.data.model.ProductSearchItem
import com.example.onlineshop.databinding.FragmentSearchBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSearch : FragmentConnectionObserver(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private val viewModel: ViewModelSearch by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        fragmentSearchQuery.doOnTextChanged { t, _, _, _ ->
            val text = t.toString()
            if (text.isNotBlank()) {
                binding.fragmentSearchPagingView.setProducer(
                    this@FragmentSearch,
                    viewModel.search(text)
                )
            }
        }
        fragmentSearchPagingView.apply {
            setup(this@FragmentSearch)
            setOnItemClickListener {
                onItemClickListener(it)
            }
        }
        fragmentSearchPagingAppbar.apply {
            setStartIconOnClickListener {
                (requireActivity() as? AppCompatActivity)?.onBackPressed()
            }
        }
    }

    private fun onItemClickListener(item: ProductSearchItem) {
        navController.navigate(
            FragmentSearchDirections.actionFragmentSearchToFragmentProductInfo(
                item.product
            )
        )
    }

    private fun observe() = with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(FragmentSearchDirections.actionFragmentSearchToFragmentNetworkConnectionFailed())
    }
}