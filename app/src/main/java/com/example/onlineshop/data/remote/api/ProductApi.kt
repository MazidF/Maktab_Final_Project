package com.example.onlineshop.data.remote.api

import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductOrders
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET("products")
    suspend fun getProductsOrderBy(
        @Query("orderby") orderBY: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Product>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category") categoryId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Product>

    @GET("products")
    suspend fun search(
        @Query("search") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Product>

    @GET("products/categories")
    suspend fun getCategories(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Category>
}