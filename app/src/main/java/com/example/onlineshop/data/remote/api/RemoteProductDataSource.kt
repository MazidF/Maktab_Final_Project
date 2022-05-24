package com.example.onlineshop.data.remote.api

import androidx.paging.*
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductOrders
import com.example.onlineshop.utils.INITIAL_SIZE
import com.example.onlineshop.utils.PAGE_SIZE
import com.example.onlineshop.utils.PRE_FETCH_DISTANCE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteProductDataSource @Inject constructor(
    private val api: ProductApi,
) {

    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        prefetchDistance = PRE_FETCH_DISTANCE,
        enablePlaceholders = true, // TODO: add shimmer
        initialLoadSize = INITIAL_SIZE,
    )

    private suspend fun getProductsOrderBy(
        orders: ProductOrders,
        page: Int,
        perPage: Int,
    ): List<Product> {
        return api.getProductsOrderBy(orders.query, page, perPage)
    }

    private suspend fun getNewestProduct(
        page: Int,
        perPage: Int,
    ): List<Product> {
        return getProductsOrderBy(ProductOrders.DATE, page, perPage)
    }

    private suspend fun getMostPopularProduct(
        page: Int,
        perPage: Int,
    ): List<Product> {
        return getProductsOrderBy(ProductOrders.POPULARITY, page, perPage)
    }

    private suspend fun getMostRatedProduct(
        page: Int,
        perPage: Int,
    ): List<Product> {
        return getProductsOrderBy(ProductOrders.RATING, page, perPage)
    }

    fun getMostRatedProduct(): Flow<PagingData<Product>> {
        return RemoteProductPagingDataSource.getPager(config = pagingConfig) { page, perPage ->
            getMostRatedProduct(page, perPage)
        }.flow
    }

    suspend fun getNewestProduct(): Flow<PagingData<Product>> {
        return RemoteProductPagingDataSource.getPager(config = pagingConfig) { page, perPage ->
            getNewestProduct(page, perPage)
        }.flow
    }

    suspend fun getMostPopularProduct(): Flow<PagingData<Product>> {
        return RemoteProductPagingDataSource.getPager(config = pagingConfig) { page, perPage ->
            getMostPopularProduct(page, perPage)
        }.flow
    }

    suspend fun getProductsByCategory(categoryId: String): Flow<PagingData<Product>> {
        return RemoteProductPagingDataSource.getPager(config = pagingConfig) { page, perPage ->
            api.getProductsByCategory(categoryId, page, perPage)
        }.flow
    }

    suspend fun search(query: String): Flow<PagingData<Product>> {
        return RemoteProductPagingDataSource.getPager(config = pagingConfig) { page, perPage ->
            api.search(query, page, perPage)
        }.flow
    }

    suspend fun getCategories(page: Int, perPage: Int): List<Category> {
        return api.getCategories(
            page = page,
            perPage = perPage,
        )
    }
}
