package com.example.onlineshop.ui.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.data.result.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin @Inject constructor(
    private val repository: ShopRepository,
    private val mainDataStore: MainDataStore,
) : ViewModel() {

    private val _customerStateFlow = MutableStateFlow<Resource<Customer>>(Resource.loading())
    val customerStateFlow get() = _customerStateFlow.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            repository.signIn(email, password).collect {
                _customerStateFlow.emit(it)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.logIn(email, password).collect {
                _customerStateFlow.emit(it)
            }
        }
    }

    fun login(customerId: Long): Job {
        return viewModelScope.launch {
            mainDataStore.updateCustomerId(customerId)
        }
    }
}