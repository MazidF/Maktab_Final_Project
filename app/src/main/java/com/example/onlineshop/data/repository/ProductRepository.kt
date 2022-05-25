package com.example.onlineshop.data.repository

import androidx.paging.PagingData
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductInfo
import com.example.onlineshop.data.remote.api.RemoteProductDataSource
import com.example.onlineshop.utils.result.SafeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository(
    private val remote: RemoteProductDataSource,
) {
    fun getMostRatedProduct(): Flow<PagingData<Product>> {
        return remote.getMostRatedProduct()
    }

    suspend fun getNewestProduct(): Flow<PagingData<Product>> {
        return remote.getNewestProduct()
    }

    suspend fun getMostPopularProduct(): Flow<PagingData<Product>> {
        return remote.getMostPopularProduct()
    }

    suspend fun getProductsByCategory(categoryId: String): Flow<PagingData<Product>> {
        return remote.getProductsByCategory(
            categoryId = categoryId,
        )
    }

    suspend fun search(query: String): Flow<PagingData<Product>> {
        return remote.search(query = query)
    }
    
    private suspend fun <T> load(reload: Boolean, block: suspend () -> SafeApiCall<T>) = flow {
        if (reload) {
            emit(SafeApiCall.reloading())
        } else {
            emit(SafeApiCall.loading())
        }
        try {
            emit(block())
        } catch (e: Exception) {
            emit(SafeApiCall.fail(e))
        }
    }

    suspend fun getCategories(reload: Boolean): Flow<SafeApiCall<List<Category>>> {
        return load(reload) {
            remote.getCategories(1, 100)
        }
    }

    suspend fun getMostPopularProduct(size: Int, reload: Boolean): Flow<SafeApiCall<List<Product>>> {
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

    suspend fun getProductInfo(productId: Int): Flow<SafeApiCall<ProductInfo>> {
        return load(false) {
            remote.getProductInfo(productId)
        }
    }
}