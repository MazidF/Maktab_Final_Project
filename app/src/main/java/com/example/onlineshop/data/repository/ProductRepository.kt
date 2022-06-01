package com.example.onlineshop.data.repository

import androidx.paging.PagingData
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductInfo
import com.example.onlineshop.data.model.ProductSearchItem
import com.example.onlineshop.data.remote.api.RemoteProductDataSource
import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.utils.result.SafeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProductRepository(
    private val remote: RemoteProductDataSource,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
) {
    fun getMostRatedProduct(): Flow<PagingData<Product>> {
        return remote.getMostRatedProduct()
    }

    fun getNewestProduct(): Flow<PagingData<Product>> {
        return remote.getNewestProduct()
    }

    fun getMostPopularProduct(): Flow<PagingData<Product>> {
        return remote.getMostPopularProduct()
    }

    fun getProductsByCategory(categoryId: String): Flow<PagingData<Product>> {
        return remote.getProductsByCategory(
            categoryId = categoryId,
        )
    }

    fun search(query: String): Flow<PagingData<ProductSearchItem>> {
        return remote.search(query = query)
    }

    private suspend fun <T> load(
        reload: Boolean,
        fakeData: T? = null,
        block: suspend () -> SafeApiCall<T>
    ): Flow<SafeApiCall<T>> = flow<SafeApiCall<T>> {
        if (reload) {
            emit(SafeApiCall.reloading())
        } else {
            emit(SafeApiCall.loading())
        }
        fakeData?.let {
            emit(SafeApiCall.success(it))
        }
        emit(block())
    }.catch { cause ->
        emit(SafeApiCall.fail(cause))
    }

    suspend fun getCategories(reload: Boolean): Flow<SafeApiCall<List<Category>>> {
        return load(reload) {
            remote.getCategories(1, 100)
        }
    }

    suspend fun getCategoriesByParentId(parentId: Long, reload: Boolean): Flow<SafeApiCall<List<Category>>> {
        return load(reload) {
            remote.getCategoriesByParentId(parentId, 1, 100)
        }
    }

    suspend fun getMostPopularProduct(
        size: Int,
        reload: Boolean
    ): Flow<SafeApiCall<List<Product>>> {
        return load(reload) {
            remote.getMostPopularProduct(1, size)
        }
    }

    suspend fun getMostRatedProduct(size: Int, reload: Boolean): Flow<SafeApiCall<List<Product>>> {
        return load(reload) {
            remote.getMostRatedProduct(1, size)
        }
    }

    suspend fun getNewestProduct(size: Int, reload: Boolean): Flow<SafeApiCall<List<Product>>> {
        return load(reload) {
            remote.getNewestProduct(1, size)
        }
    }

    suspend fun getProductInfo(productId: Long): Flow<SafeApiCall<ProductInfo>> {
        return load(false) {
            remote.getProductInfo(productId)
        }
    }

    suspend fun getProductById(ids: Array<Long>): Flow<SafeApiCall<List<Product>>> {
        val seed = System.currentTimeMillis()
        return load(false, List(ids.size) {
            Product.fake(seed + it)
        }) {
            remote.getProductById(ids)
        }
    }
}