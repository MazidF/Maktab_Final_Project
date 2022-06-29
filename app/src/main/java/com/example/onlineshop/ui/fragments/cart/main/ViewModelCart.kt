package com.example.onlineshop.ui.fragments.cart.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.data.model.order.SimpleLineItem
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.ui.model.OrderItem
import com.example.onlineshop.utils.toSimpleLineItem
import com.example.onlineshop.utils.toSimpleOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelCart @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {
    private val customerId = repository.customer.id

    fun loadLastOrders(userId: Long): Flow<Resource<List<Order>>> {
        return repository.getOrders(userId)
    }

    fun addToCart(productId: Long, cb: (Int) -> Unit) {
        val lineItem = getLineItemById(productId)
        updateCart(lineItem, lineItem.count + 1, cb)
    }

    fun addToCart(lineItem: SimpleLineItem, cb: (Int) -> Unit) {
        updateCart(lineItem, lineItem.count + 1, cb)
    }

    fun removeFromCart(productId: Long, cb: (Int) -> Unit) {
        val lineItem = getLineItemById(productId)
        updateCart(lineItem, lineItem.count - 1, cb)
    }

    fun removeFromCart(lineItem: SimpleLineItem, cb: (Int) -> Unit) {
        updateCart(lineItem, lineItem.count - 1, cb)
    }

    fun updateCart(productId: Long, count: Int, cb: (Int) -> Unit) {
        val lineItem = getLineItemById(productId)
        return updateCart(lineItem, count, cb)
    }

    fun updateCart(lineItem: SimpleLineItem, count: Int, cb: (Int) -> Unit) {
        val lastOrder = repository.currentOrder.toSimpleOrder()
        val newOrder = lastOrder + lineItem.copy(count)
        viewModelScope.launch {
            repository.updateOrder(newOrder).asSuccess()?.let { success ->
                success.body().let { orderItem ->
                    val newCount = orderItem.lineItems.map {
                        it.lineItem
                    }.firstOrNull { item ->
                        item.productId == lineItem.productId
                    }?.count ?: lineItem.count
                    cb(newCount)
                }
            } ?: run {
                cb(lineItem.count)
            }
        }
    }

    private fun getLineItemById(id: Long): SimpleLineItem {
        val order = repository.currentOrder
        return order.lineItems.firstOrNull {
            it.product.id == id
        }?.toSimpleLineItem() ?: SimpleLineItem(-1, id, 0)
    }

    fun refreshOrder(): LiveData<OrderItem> {
        throw Exception(":)")
    }

    fun currentOrder(): OrderItem {
        return repository.currentOrder
    }

}
