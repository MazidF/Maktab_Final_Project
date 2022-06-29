package com.example.onlineshop.ui.fragments.orders.sublist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentOrderListBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.fragments.orders.FragmentOrderHistoryArgs
import com.example.onlineshop.ui.fragments.orders.ViewModelOrderHistory
import com.example.onlineshop.ui.model.OrderItem
import com.example.onlineshop.utils.launchOnState

class FragmentOrderList : FragmentConnectionObserver(R.layout.fragment_order_list) {

    private var _binding: FragmentOrderListBinding? = null
    private val binding: FragmentOrderListBinding
        get() = _binding!!

    private val viewModel: ViewModelOrderHistory by activityViewModels()
    private lateinit var orderAdapter: OrderPagingAdapter
    private val args: FragmentOrderHistoryArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderListBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        orderAdapter = OrderPagingAdapter(
            onItemClick = this@FragmentOrderList::onItemClick,
        )
        orderListList.apply {
            adapter = orderAdapter
        }
    }

    private fun onItemClick(item: OrderItem) {

    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.loadOrdersByStatus(args.orderStatus).collect {
                orderAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(
            FragmentOrderListDirections.actionFragmentOrderListToFragmentNetworkConnectionFailed()
        )
    }
}
