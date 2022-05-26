package com.example.onlineshop.ui.fragments.product_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductInfo
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.result.SafeApiCall
import com.example.onlineshop.utils.transformer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelProductInfo @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _productInfoStateFlow = MutableStateFlow<SafeApiCall<ProductInfo>>(
        SafeApiCall.loading()
    )
    val productInfoStateFlow get() = _productInfoStateFlow.asStateFlow()

    private val _similarStateFlow = MutableStateFlow<SafeApiCall<List<ProductListItem>>>(
        SafeApiCall.loading()
    )
    val similarStateFlow get() = _similarStateFlow.asStateFlow()

    fun loadProductInfo(product: Product) {
        viewModelScope.launch {
            repository.getProductInfo(product.id).collect {
                if (it.isSuccessful) {
                    loadSimilar((it as SafeApiCall.Success).body().relatedList)
                }
                _productInfoStateFlow.emit(it)
            }
        }
    }

    private fun loadSimilar(ids: List<Long>) {
        viewModelScope.launch {
            repository.getProductById(ids.toTypedArray()).map(transformer).collect {
                _similarStateFlow.emit(it)
            }
        }
    }
}
