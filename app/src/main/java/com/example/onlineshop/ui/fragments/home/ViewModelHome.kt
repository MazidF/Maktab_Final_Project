package com.example.onlineshop.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.result.SafeApiCall
import com.example.onlineshop.utils.productToProductListItemTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private typealias SafeProducts = SafeApiCall<List<ProductListItem>>

@HiltViewModel
class ViewModelHome @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {
    var hasBeenLoaded = false
        private set

    private val _mostPopularProductListFLowState = MutableStateFlow<SafeProducts>(
        SafeApiCall.loading()
    )
    val mostPopularProductListFlowState get() = _mostPopularProductListFLowState.asStateFlow()

    private val _mostRatedProductListFLowState = MutableStateFlow<SafeProducts>(
        SafeApiCall.loading()
    )
    val mostRatedProductListFlowState get() = _mostRatedProductListFLowState.asStateFlow()

    private val _newestProductListFLowState = MutableStateFlow<SafeProducts>(
        SafeApiCall.loading()
    )
    val newestProductListFlowState get() = _newestProductListFLowState.asStateFlow()


    private fun getPopularAsync(): Deferred<SafeProducts> {
        return viewModelScope.async {
            repository.getMostPopularProduct(15, hasBeenLoaded).map(productToProductListItemTransformer).collect {
                _mostPopularProductListFLowState.emit(it)
            }
            _mostPopularProductListFLowState.value
        }
    }

    private fun getNewestAsync(): Deferred<SafeProducts> {
        return viewModelScope.async {
            repository.getNewestProduct(15, hasBeenLoaded).map(productToProductListItemTransformer).collect {
                _newestProductListFLowState.emit(it)
            }
            _newestProductListFLowState.value
        }
    }

    private fun getRatedAsync(): Deferred<SafeProducts> {
        return viewModelScope.async {
            repository.getMostRatedProduct(15, hasBeenLoaded).map(productToProductListItemTransformer).collect {
                _mostRatedProductListFLowState.emit(it)
            }
            val a = _mostRatedProductListFLowState.value
            return@async a
        }
    }

    // TODO: load unloaded lists not all
    fun loadDataAsync(): Deferred<Boolean> {
        return viewModelScope.async {
            val list = listOf(
                getRatedAsync(),
                getNewestAsync(),
                getPopularAsync(),
            )
            val wasSuccessful = list.awaitAll().map {
                it.isSuccessful
            }.reduce { a, b -> a.and(b) }
            wasSuccessful.also {
                hasBeenLoaded = it
            }
        }
    }
}