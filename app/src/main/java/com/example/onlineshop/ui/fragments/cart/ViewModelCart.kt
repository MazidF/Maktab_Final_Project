package com.example.onlineshop.ui.fragments.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.ui.model.ProductCartItem
import com.example.onlineshop.utils.getWithDefault
import com.example.onlineshop.utils.result.SafeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelCart @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private var mapChanged = false

    private val _productStateFlow =
        MutableStateFlow<SafeApiCall<List<ProductCartItem>>>(SafeApiCall.loading())
    val productStateFlow get() = _productStateFlow.asStateFlow()

    private lateinit var map: HashMap<Long, Int>
    private var hasBeenLoaded = false

    init {
        viewModelScope.launch {
            map = repository.loadCartMap()
            val a = map
        }
    }

    fun addToCart(productId: Long, count: Int = 1) {
        mapChanged = true
        map[productId] = map.getWithDefault(productId, 0) + count
    }

    fun removeFromCart(productId: Long, count: Int = 1) {
        mapChanged = true
        map[productId] = map[productId]!! - count
    }

    fun getProductCount(productId: Long): Int {
        mapChanged = true
        return map[productId] ?: 0
    }

    fun updateCart(productId: Long, count: Int) {
        mapChanged = true
        if (count == 0) {
            map.remove(productId)
        } else {
            map[productId] = count
        }
    }

    fun loadProducts() {
        if (hasBeenLoaded.not() || mapChanged) {
            mapChanged = false
            getProducts()
        }/* else {
            retry()
        }*/
    }

    fun retry() {
        hasBeenLoaded = false
        loadProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            val list = map.keys.toList().toTypedArray()
            repository.getProductById(list, false).collect {
                _productStateFlow.emit(
                    it.map { mapList ->
                        mapList.map { product ->
                            ProductCartItem(
                                product = product,
                                count = map[product.id] ?: 0
                            )
                        }
                    }
                )
                hasBeenLoaded = true
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            repository.saveCartMap(map)
        }
    }
}
