package com.example.onlineshop.ui.fragments.cart.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.model.order.LineItem
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
    private val mainDataStore: MainDataStore,
) : ViewModel() {

    private var failedCounter: Int = 0
    private var retryJob: Job? = null

    private val _orderStateFlow = MutableStateFlow<Resource<OrderItem>>(
        Resource.loading()
    )
    val orderStateFlow get() = _orderStateFlow.asStateFlow()

    private val _customerIdStateFlow = MutableStateFlow(-1L)

    init {
        viewModelScope.launch {
            mainDataStore.preferences.collect { info ->
                _customerIdStateFlow.emit(info.userId)
                loadCurrentOrder(info.userId)
            }
        }
    }

    fun loadLastOrders(userId: Long): Flow<PagingData<Order>> {
        return repository.getFinishedOrders(userId)
    }

    private suspend fun loadCurrentOrder(userId: Long) {
        repository.getPendingOrder(userId).collect {
            _orderStateFlow.emit(it)
            if (it is Resource.Fail) {
                failedCounter++
                if (failedCounter > 3) {
                    retry()
                }
            } else if (it is Resource.Success) {
                failedCounter = 0
            }
        }
    }

    fun addToCart(productId: Long, cb: (Int) -> Unit) {
        val lineItem = getLineItemById(productId) ?: return
        updateCart(lineItem, lineItem.count + 1, cb)
    }

    fun addToCart(lineItem: SimpleLineItem, cb: (Int) -> Unit) {
        updateCart(lineItem, lineItem.count + 1, cb)
    }

    fun removeFromCart(productId: Long, cb: (Int) -> Unit) {
        val lineItem = getLineItemById(productId) ?: return
        updateCart(lineItem, lineItem.count - 1, cb)
    }

    fun removeFromCart(lineItem: SimpleLineItem, cb: (Int) -> Unit) {
        updateCart(lineItem, lineItem.count - 1, cb)
    }

    fun updateCart(productId: Long, count: Int, cb: (Int) -> Unit) {
        val lineItem = getLineItemById(productId) ?: return
        return updateCart(lineItem, count, cb)
    }

    fun updateCart(lineItem: SimpleLineItem, count: Int, cb: (Int) -> Unit) {
        val lastOrder = lastOrder()!!.toSimpleOrder()
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
                _orderStateFlow.emit(success)
            }
        }
    }

    private fun lastOrder(): Order? {
        return _orderStateFlow.value.asSuccess()?.body()?.toOrder()
    }

    private fun getLineItemById(id: Long): SimpleLineItem? {
        val order = lastOrder()
        return order?.run {
            lineItems.firstOrNull {
                it.productId == id
            }?.toSimpleLineItem() ?: SimpleLineItem(id, 0)
        }
    }

    fun retry(): Job {
        if (retryJob == null) {
            retryJob = viewModelScope.launch {
                val userId = mainDataStore.preferences.first().userId
                loadCurrentOrder(userId)
                retryJob = null
            }
        }
        return retryJob!!
    }

}
