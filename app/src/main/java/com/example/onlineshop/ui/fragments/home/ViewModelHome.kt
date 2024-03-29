package com.example.onlineshop.ui.fragments.home

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.model.ProductImages
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.utils.productToProductListItemTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private typealias SafeProducts = Resource<List<ProductListItem>>

@HiltViewModel
class ViewModelHome @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {
    var hasBeenLoaded = false
        private set

    private val _mostPopularProductListFLowState = MutableStateFlow<SafeProducts>(
        Resource.loading()
    )
    val mostPopularProductListFlowState get() = _mostPopularProductListFLowState.asStateFlow()

    private val _mostRatedProductListFLowState = MutableStateFlow<SafeProducts>(
        Resource.loading()
    )
    val mostRatedProductListFlowState get() = _mostRatedProductListFLowState.asStateFlow()

    private val _newestProductListFLowState = MutableStateFlow<SafeProducts>(
        Resource.loading()
    )
    val newestProductListFlowState get() = _newestProductListFLowState.asStateFlow()

    private val _imagesFlowState = MutableStateFlow<Resource<ProductImages>>(Resource.loading())
    val imagesFlowState get() = _imagesFlowState.asStateFlow()

    private fun getImagesAsync(): Deferred<Resource<ProductImages>> {
        return viewModelScope.async {
            repository.getMainPosterProducts().also {
                _imagesFlowState.emit(it)
            }
            _imagesFlowState.value
        }
    }

    private fun getPopularAsync(): Deferred<SafeProducts> {
        return viewModelScope.async {
            repository.getMostPopularProduct(15, hasBeenLoaded)
                .map(productToProductListItemTransformer).collect {
                _mostPopularProductListFLowState.emit(it)
            }
            _mostPopularProductListFLowState.value
        }
    }

    private fun getNewestAsync(): Deferred<SafeProducts> {
        return viewModelScope.async {
            repository.getNewestProduct(15, hasBeenLoaded).map(productToProductListItemTransformer)
                .collect {
                    _newestProductListFLowState.emit(it)
                }
            _newestProductListFLowState.value
        }
    }

    private fun getRatedAsync(): Deferred<SafeProducts> {
        return viewModelScope.async {
            repository.getMostRatedProduct(15, hasBeenLoaded)
                .map(productToProductListItemTransformer).collect {
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
                getImagesAsync(),
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

    private var states: List<Parcelable?>? = null

    fun putState(states: List<Parcelable?>) {
        this.states = states
    }

    fun getState(): List<Parcelable?>? {
        return states.also {
            this.states = null
        }
    }
}