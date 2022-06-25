package com.example.onlineshop.ui.fragments.productinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductInfo
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.utils.productToProductListItemTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: refactor to livedata or not!!!

@HiltViewModel
class ViewModelProductInfo @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    private val _productInfoStateFlow = MutableStateFlow<Resource<ProductInfo>>(
        Resource.loading()
    )
    val productInfoStateFlow get() = _productInfoStateFlow.asStateFlow()

    private val _similarStateFlow = MutableStateFlow<Resource<List<ProductListItem>>>(
        Resource.loading()
    )
    val similarStateFlow get() = _similarStateFlow.asStateFlow()

    fun loadProductInfo(product: Product) {
        viewModelScope.launch {
            repository.getProductInfo(product.id).collect {
                if (it.isSuccessful) {
                    loadSimilar((it as Resource.Success).body().relatedList)
                }
                _productInfoStateFlow.emit(it)
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
