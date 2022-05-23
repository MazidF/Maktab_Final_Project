package com.example.online_shop.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

enum class ProductOrders(val query: String) {
    ID(query = "id"),
    DATE(query = "date"),
    PRICE(query = "price"),
    RATING(query = "rating"),
    POPULARITY(query = "popularity");

    override fun toString() = query
}

interface ProductApi {

    @GET("products")
    suspend fun getProductsOrderBy(
        @Query("orderby") orderBY: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    )

    private suspend fun getProductsOrderBy(
        orders: ProductOrders,
        page: Int,
        perPage: Int,
    ) {
        return getProductsOrderBy(orders.query, page, perPage)
    }

    suspend fun getNewestProduct(
        page: Int,
        perPage: Int,
    ) {
        return getProductsOrderBy(ProductOrders.DATE, page, perPage)
    }

    suspend fun getMostPopularProduct(
        page: Int,
        perPage: Int,
    ) {
        return getProductsOrderBy(ProductOrders.POPULARITY, page, perPage)
    }

    suspend fun getMostRatedProduct(
        page: Int,
        perPage: Int,
    ) {
        return getProductsOrderBy(ProductOrders.RATING, page, perPage)
    }

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category") categoryId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    )

    @GET("products")
    suspend fun search(
        @Query("search") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    )

    @GET("products/categories")
    suspend fun getCategories(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    )
}