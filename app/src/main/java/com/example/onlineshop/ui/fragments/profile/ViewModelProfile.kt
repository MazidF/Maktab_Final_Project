package com.example.onlineshop.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.local.data_store.main.MainInfo
import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.utils.result.SafeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelProfile @Inject constructor(
    private val repository: ProductRepository,
    private val mainDataStore: MainDataStore,
) : ViewModel() {

    private lateinit var mainInfoStateFlow: StateFlow<MainInfo>

    private val _customerStateFlow = MutableStateFlow<SafeApiCall<Customer>>(SafeApiCall.loading())
    val customerStateFlow get() = _customerStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            mainInfoStateFlow = mainDataStore.preferences.stateIn(viewModelScope)
        }
    }

    fun getCustomer(customerId: Long) {
        viewModelScope.launch {
            repository.getCustomerById(customerId, false).collect {
                _customerStateFlow.emit(it)
            }
        }
    }
}
