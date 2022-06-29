package com.example.onlineshop.ui.fragments.orders

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.data.model.order.OrderStatus
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.ui.model.OrderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelOrderHistory @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {
    val customerId = repository.customer.id

    fun loadOrdersByStatus(orderStatus: OrderStatus): Flow<PagingData<OrderItem>> {
        return repository.getOrders(customerId, orderStatus)
    }
}
