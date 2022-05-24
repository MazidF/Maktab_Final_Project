package com.example.onlineshop.data.repository

import androidx.paging.PagingData
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.remote.api.RemoteProductDataSource
import kotlinx.coroutines.flow.Flow

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

    suspend fun getCategories(): List<Category> {
        return remote.getCategories(1, 100)
    }
}