package com.example.onlineshop.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.ui.model.ProductListItem.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelHome @Inject constructor(
    private val repository: ProductRepository,
): ViewModel() {

    private val _someProductListFLowState = MutableStateFlow<List<ProductListItem>>(listOf())
    val someProductListFlowState get() = _someProductListFLowState.asStateFlow()

    init {
        getSomeProductList()
    }

    private fun getSomeProductList() {
        viewModelScope.launch {
            val list = repository.getMostPopularProduct(15).map {
                Item(it)
            }
            _someProductListFLowState.emit(list)
        }
    }

    fun getMostRatedProduct(): Flow<PagingData<Item>> {
        return repository.getMostRatedProduct()
            .map { pagingData ->
                pagingData.map {
                    Item(it)
                }
            }
            .cachedIn(viewModelScope)
    }
}