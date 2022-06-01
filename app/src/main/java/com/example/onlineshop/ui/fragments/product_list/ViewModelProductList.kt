package com.example.onlineshop.ui.fragments.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.ui.model.ProductList
import com.example.onlineshop.ui.model.ProductListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ViewModelProductList @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private var pagingDataFlow: Flow<PagingData<ProductListItem.Item>>? = null

    fun load(productList: ProductList): Flow<PagingData<ProductListItem.Item>> {
        return pagingDataFlow ?: when(productList) {
            is ProductList.ByCategory -> {
                repository.getProductsByCategory(productList.category.id.toString())
            }
            ProductList.MostPopular -> {
                repository.getMostPopularProduct()
            }
            ProductList.MostRated -> {
                repository.getMostRatedProduct()
            }
            ProductList.Newest -> {
                repository.getNewestProduct()
            }
        }.map { pagingData ->
            pagingData.map {
                ProductListItem.Item(it)
            }
        }.also {
            pagingDataFlow = it
        }.cachedIn(viewModelScope)
    }

}
