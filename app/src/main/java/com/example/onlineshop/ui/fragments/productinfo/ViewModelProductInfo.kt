package com.example.onlineshop.ui.fragments.productinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductInfo
import com.example.onlineshop.data.model.ProductReview
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.utils.productToProductListItemTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelProductInfo @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {
    private var hasBeenLoaded = false
    private var loaderJob: Job? = null

    private val _productInfoStateFlow = MutableStateFlow<Resource<ProductInfo>>(
        Resource.loading()
    )
    val productInfoStateFlow get() = _productInfoStateFlow.asStateFlow()

    private val _similarStateFlow = MutableStateFlow<Resource<List<ProductListItem>>>(
        Resource.loading()
    )
    val similarStateFlow get() = _similarStateFlow.asStateFlow()

    private val _reviewsStateFlow = MutableStateFlow<Resource<List<ProductReview>>>(
        Resource.loading()
    )
    val reviewsStateFlow get() = _reviewsStateFlow.asStateFlow()

    fun loadProductInfo(product: Product) {
        if (loaderJob != null || hasBeenLoaded) return
        val id = product.id
        loadProductReviews(id)
        loaderJob = viewModelScope.launch {
            repository.getProductInfo(id).collect {
                if (it is Resource.Success) {
                    hasBeenLoaded = true
                    val info = it.body()
                    loadSimilar(info.relatedList)
                }
                if (it is Resource.Success || it is Resource.Fail) {
                    loaderJob = null
                }
                _productInfoStateFlow.emit(it)
            }
        }
    }

    private fun loadProductReviews(productId: Long) {
        viewModelScope.launch {
            repository.getReviewOfProduct(productId, 4).collect {
                _reviewsStateFlow.emit(it)
            }
        }
    }

    private fun loadSimilar(ids: List<Long>) {
        viewModelScope.launch {
            repository.getProductById(ids.toTypedArray()).map(productToProductListItemTransformer).collect {
                _similarStateFlow.emit(it)
            }
        }
    }
}
