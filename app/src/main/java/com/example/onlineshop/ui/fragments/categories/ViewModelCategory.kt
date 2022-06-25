package com.example.onlineshop.ui.fragments.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.ui.model.CategoryListItem
import com.example.onlineshop.utils.categoryToCategoryListItemTransformer
import com.example.onlineshop.utils.getWithDefault
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.data.result.Resource.Companion.fail
import com.example.onlineshop.data.result.Resource.Companion.loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

private typealias SafeCategoryList = Resource<List<CategoryListItem>>

@HiltViewModel
class ViewModelCategory @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    private val _categoryTitlesStateFlow = MutableStateFlow<SafeCategoryList>(
        loading()
    )
    val categoriesStateFlow get() = _categoryTitlesStateFlow.asStateFlow()

    val categoryMap = HashMap<Long, ArrayList<CategoryListItem>>()

    init {
        loadCategoriesTitles()
    }

    private fun loadCategoriesTitles() {
        viewModelScope.launch {
            repository.getCategoriesByParentId(0, false).collect {
                val list = categoryToCategoryListItemTransformer(it)
                if (it is Resource.Loading || refresh(list)) {
                    _categoryTitlesStateFlow.emit(list)
                } else {
                    _categoryTitlesStateFlow.emit(fail(Exception("Failed to load categories!!")))
                }
            }
        }
    }

    private suspend fun refresh(sac: SafeCategoryList): Boolean {
        sac.asSuccess()?.let { _ ->
            val list = repository.getCategories(false).first {
                when(it) {
                    is Resource.Fail, is Resource.Success -> true
                    else -> false
                }
            }.asSuccess()?.body() ?: return false
            list.forEach {
                categoryMap.getWithDefault(it.parent, ArrayList()).add(CategoryListItem.Item(it))
            }
            return true
        }
        return false
    }
}
