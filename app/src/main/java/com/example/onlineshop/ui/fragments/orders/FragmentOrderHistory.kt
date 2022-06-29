package com.example.onlineshop.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentOrderHistoryBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentOrderHistory : FragmentConnectionObserver(R.layout.fragment_order_history) {
    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding: FragmentOrderHistoryBinding
        get() = _binding!!

    private val viewModel: ViewModelOrderHistory by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderHistoryBinding.bind(view)

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
        navController.navigate(
            FragmentOrderHistoryDirections.actionFragmentOrderHistoryToFragmentNetworkConnectionFailed()
        )
    }
}