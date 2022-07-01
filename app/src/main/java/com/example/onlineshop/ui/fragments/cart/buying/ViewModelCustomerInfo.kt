package com.example.onlineshop.ui.fragments.cart.buying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.model.Coupon
import com.example.onlineshop.data.model.customer.CustomerInfo
import com.example.onlineshop.data.model.order.OrderStatus
import com.example.onlineshop.data.model.order.SimpleOrder
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.data.result.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelCustomerInfo @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    val order = repository.currentOrder
    val customer = repository.customer

    private val _infoStateFlow = MutableStateFlow<Resource<CustomerInfo>>(
        Resource.loading()
    )
    val infoStateFlow get() = _infoStateFlow.asStateFlow()

    init {
        loadLocation()
    }

    fun loadLocation() {
        viewModelScope.launch {
            repository.getCustomerInfo(repository.customer.id).collect {
                _infoStateFlow.emit(it)
            }
        }
    }

    fun getCoupon(code: String): Flow<Resource<Coupon?>> {
        return repository.getCoupon(code)
    }

    fun completeOrder(coupon: Coupon?, note: String): Flow<Boolean> {
        return repository.completeOrder(coupon, note)
    }

}
