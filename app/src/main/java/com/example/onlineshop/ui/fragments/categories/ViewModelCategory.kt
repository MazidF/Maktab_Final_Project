package com.example.onlineshop.ui.fragments.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.utils.result.SafeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelCategory @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _categoriesStateFlow = MutableStateFlow<SafeApiCall<List<Category>>>(
        SafeApiCall.loading()
    )
    val categoriesStateFlow get() = _categoriesStateFlow.asStateFlow()

    init {
        loadCategories(false)
    }

    fun loadCategories() {
        loadCategories(true)
    }

    private fun loadCategories(reload: Boolean) {
        viewModelScope.launch {
            repository.getCategories(reload).collect {
                _categoriesStateFlow.emit(it)
            }
        }
    }
}
