package com.example.onlineshop.ui.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.onlineshop.data.model.ProductSearchItem
import com.example.onlineshop.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelSearch @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    fun search(query: String): Flow<PagingData<ProductSearchItem>> {
        return repository.search(query)
            .cachedIn(viewModelScope)
    }

}
