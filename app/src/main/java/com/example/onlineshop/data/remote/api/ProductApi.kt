package com.example.onlineshop.data.remote.api

import com.example.onlineshop.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("products/{id}")
    suspend fun getProductInfo(
        @Path("id") id: String,
    ): Response<ProductInfo>

    @GET("products")
    suspend fun getProductsById(
        @Query("include") ids: String,
    ): Response<List<Product>>

    @GET("products")
    suspend fun getProductsOrderBy(
        @Query("orderby") orderBY: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<Product>>

    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category") categoryId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<Product>>

    @GET("products")
    suspend fun search(
        @Query("search") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<ProductSearchItem>>

    @GET("products/categories")
    suspend fun getCategories(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<Category>>

    @GET("products/categories")
    suspend fun getCategoriesByParentId(
        @Query("parent") parentId: Long,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<List<Category>>
}