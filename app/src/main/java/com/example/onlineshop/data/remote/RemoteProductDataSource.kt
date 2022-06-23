package com.example.onlineshop.data.remote

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.onlineshop.data.model.*
import com.example.onlineshop.data.remote.api.ProductApi
import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.utils.INITIAL_SIZE
import com.example.onlineshop.utils.PAGE_SIZE
import com.example.onlineshop.utils.PRE_FETCH_DISTANCE
import com.example.onlineshop.utils.asResource
import com.example.onlineshop.data.result.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteProductDataSource @Inject constructor(
    private val api: ProductApi,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
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
    ): Resource<List<Product>> {
        return api.getProductsOrderBy(orders.query, page, perPage).asResource()
    }

    suspend fun getNewestProduct(
        page: Int,
        perPage: Int,
    ): Resource<List<Product>> {
        return getProductsOrderBy(ProductOrders.DATE, page, perPage)
    }

    suspend fun getMostPopularProduct(
        page: Int,
        perPage: Int,
    ): Resource<List<Product>> {
        return getProductsOrderBy(ProductOrders.POPULARITY, page, perPage)
    }

    suspend fun getMostRatedProduct(
        page: Int,
        perPage: Int,
    ): Resource<List<Product>> {
        return getProductsOrderBy(ProductOrders.RATING, page, perPage)
    }

    fun getMostRatedProduct(): Flow<PagingData<Product>> {
        return RemotePagingSource.getPager(config = pagingConfig) { page, perPage ->
            getMostRatedProduct(page, perPage)
        }.flow.flowOn(dispatcher)
    }

    fun getNewestProduct(): Flow<PagingData<Product>> {
        return RemotePagingSource.getPager(config = pagingConfig) { page, perPage ->
            getNewestProduct(page, perPage)
        }.flow.flowOn(dispatcher)
    }

    fun getMostPopularProduct(): Flow<PagingData<Product>> {
        return RemotePagingSource.getPager(config = pagingConfig) { page, perPage ->
            getMostPopularProduct(page, perPage)
        }.flow.flowOn(dispatcher)
    }

    fun getProductsByCategory(categoryId: String): Flow<PagingData<Product>> {
        return RemotePagingSource.getPager(config = pagingConfig) { page, perPage ->
            api.getProductsByCategory(categoryId, page, perPage).asResource()
        }.flow.flowOn(dispatcher)
    }

    fun search(query: String): Flow<PagingData<ProductSearchItem>> {
        return RemotePagingSource.getPager(config = pagingConfig) { page, perPage ->
            api.search(query, page, perPage).asResource()
        }.flow.flowOn(dispatcher)
    }

    suspend fun getCategories(page: Int, perPage: Int): Resource<List<Category>> {
        return api.getCategories(
            page = page,
            perPage = perPage,
        ).asResource()
    }

    suspend fun getCategoriesByParentId(
        parentId: Long,
        page: Int,
        perPage: Int
    ): Resource<List<Category>> {
        return api.getCategoriesByParentId(
            parentId = parentId,
            page = page,
            perPage = perPage,
        ).asResource()
    }

    suspend fun getProductInfo(productId: Long): Resource<ProductInfo> {
        return api.getProductInfo(productId.toString()).asResource()
    }

    suspend fun getProductById(ids: Array<Long>): Resource<List<Product>> {
        return api.getProductsById(
            ids.contentToString().replace("[", "[0, ")
        ).asResource()
    }

    suspend fun getMainPosterProducts(): Resource<ProductImages> {
        return api.getMainPosterProducts().asResource()
    }
}
