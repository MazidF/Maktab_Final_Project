package com.example.onlineshop.data.remote.api

import androidx.paging.*
import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductInfo
import com.example.onlineshop.data.model.ProductOrders
import com.example.onlineshop.utils.INITIAL_SIZE
import com.example.onlineshop.utils.PAGE_SIZE
import com.example.onlineshop.utils.PRE_FETCH_DISTANCE
import com.example.onlineshop.utils.asSafeApiCall
import com.example.onlineshop.utils.result.SafeApiCall
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.util.*
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
    ): SafeApiCall<List<Product>> {
        return api.getProductsOrderBy(orders.query, page, perPage).asSafeApiCall()
    }

    suspend fun getNewestProduct(
        page: Int,
        perPage: Int,
    ): SafeApiCall<List<Product>> {
        return getProductsOrderBy(ProductOrders.DATE, page, perPage)
    }

    suspend fun getMostPopularProduct(
        page: Int,
        perPage: Int,
    ): SafeApiCall<List<Product>> {
        return getProductsOrderBy(ProductOrders.POPULARITY, page, perPage)
    }

    suspend fun getMostRatedProduct(
        page: Int,
        perPage: Int,
    ): SafeApiCall<List<Product>> {
        return getProductsOrderBy(ProductOrders.RATING, page, perPage)
    }

    fun getMostRatedProduct(): Flow<PagingData<Product>> {
        return RemoteProductPagingSource.getPager(config = pagingConfig) { page, perPage ->
            getMostRatedProduct(page, perPage)
        }.flow
    }

    suspend fun getNewestProduct(): Flow<PagingData<Product>> {
        return RemoteProductPagingSource.getPager(config = pagingConfig) { page, perPage ->
            getNewestProduct(page, perPage)
        }.flow
    }

    suspend fun getMostPopularProduct(): Flow<PagingData<Product>> {
        return RemoteProductPagingSource.getPager(config = pagingConfig) { page, perPage ->
            getMostPopularProduct(page, perPage)
        }.flow
    }

    suspend fun getProductsByCategory(categoryId: String): Flow<PagingData<Product>> {
        return RemoteProductPagingSource.getPager(config = pagingConfig) { page, perPage ->
            api.getProductsByCategory(categoryId, page, perPage).asSafeApiCall()
        }.flow
    }

    suspend fun search(query: String): Flow<PagingData<Product>> {
        return RemoteProductPagingSource.getPager(config = pagingConfig) { page, perPage ->
            api.search(query, page, perPage).asSafeApiCall()
        }.flow
    }

    suspend fun getCategories(page: Int, perPage: Int): SafeApiCall<List<Category>> {
        return api.getCategories(
            page = page,
            perPage = perPage,
        ).asSafeApiCall()
    }

    suspend fun getProductInfo(productId: Long): SafeApiCall<ProductInfo> {
        return api.getProductInfo(productId.toString()).asSafeApiCall()
    }

    suspend fun getProductById(ids: Array<Long>): SafeApiCall<List<Product>> {
        return api.getProductsById(ids.contentToString()).asSafeApiCall()
    }
}
