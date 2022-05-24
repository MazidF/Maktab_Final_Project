package com.example.onlineshop.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelHome @Inject constructor(
    private val repository: ProductRepository,
): ViewModel() {

    fun getMostRatedProduct(): Flow<PagingData<Product>> {
        return repository.getMostRatedProduct()
            .cachedIn(viewModelScope)
    }
}