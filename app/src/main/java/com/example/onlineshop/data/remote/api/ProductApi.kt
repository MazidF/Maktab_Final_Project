package com.example.onlineshop.data.remote.api

import com.example.onlineshop.data.model.*
import retrofit2.Response
import retrofit2.http.*

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
    suspend fun getProductByDate(
        @Query("after") date: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
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

    @GET("products/608")
    suspend fun getMainPosterProducts(): Response<ProductImages>

    @POST("products/reviews")
    suspend fun createReview(
        @Body review: ProductReview,
    ): Response<ProductReview>

    @GET("products/reviews/{id}")
    suspend fun getReview(
        @Path("id") reviewId: Long,
    ): Response<ProductReview>

    @GET("products/reviews")
    suspend fun getReviewOfProduct(
        @Query("product") productId: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): Response<List<ProductReview>>
}