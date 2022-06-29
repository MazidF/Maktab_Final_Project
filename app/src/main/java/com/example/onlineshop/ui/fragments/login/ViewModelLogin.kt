package com.example.onlineshop.ui.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.data.result.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    suspend fun signIn(email: String, password: String): Flow<Boolean> {
        return repository.signIn(email, password)
    }

    suspend fun login(email: String, password: String): Flow<Boolean> {
        return repository.logIn(email, password)
    }

    suspend fun login(customerId: Long): Flow<Boolean> {
        return repository.logIn(customerId)
    }
}