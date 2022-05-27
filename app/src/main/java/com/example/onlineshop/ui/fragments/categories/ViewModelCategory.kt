package com.example.onlineshop.ui.fragments.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.ui.model.CategoryListItem
import com.example.onlineshop.utils.categoryToCategoryListItemTransformer
import com.example.onlineshop.utils.getWithDefault
import com.example.onlineshop.utils.result.SafeApiCall
import com.example.onlineshop.utils.result.SafeApiCall.Companion.loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private typealias SafeCategoryList = SafeApiCall<List<CategoryListItem>>

@HiltViewModel
class ViewModelCategory @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _categoryTitlesStateFlow = MutableStateFlow<SafeCategoryList>(
        loading()
    )
    val categoriesStateFlow get() = _categoryTitlesStateFlow.asStateFlow()

    val categoryMap = HashMap<Long, MutableStateFlow<SafeCategoryList>>()

    init {
        loadCategoriesTitles()
    }

    private fun loadCategoriesTitles() {
        viewModelScope.launch {
            repository.getCategoriesByParentId(0, false).collect {
                val list = categoryToCategoryListItemTransformer(it)
                refresh(list)
                _categoryTitlesStateFlow.emit(list)
            }
        }
    }

    private suspend fun refresh(sac: SafeCategoryList) {
        sac.asSuccess()?.let { apiCall ->
            for (i in apiCall.body()) {
                loadCategoriesByParent(i.category.id)
            }
        }
    }

    private suspend fun loadCategoriesByParent(parentId: Long) {
        repository.getCategoriesByParentId(parentId, true).collect {
            categoryMap.getWithDefault(parentId, MutableStateFlow(loading()))
                .emit(categoryToCategoryListItemTransformer(it))
        }
    }
}
