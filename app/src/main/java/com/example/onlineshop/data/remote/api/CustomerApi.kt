package com.example.onlineshop.data.remote.api

import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.model.customer.RawCustomer
import com.example.onlineshop.data.model.order.Order
import retrofit2.http.*

interface CustomerApi {

    @POST("customers")
    suspend fun createCustomer(
        @Body customer: RawCustomer,
    ) : Customer

    @PUT("customers/{id}")
    suspend fun updateCustomer(
        @Path("id") id: Long,
    ) : Customer

    @GET("customers/{id}")
    suspend fun getCustomer(
        @Path("id") id: Long,
    ) : Customer

    @POST("orders")
    suspend fun createOrder(
        @Body customer: Customer,
    ) : Order

    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Long,
    ) : Order

    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") id: Long,
    ) : Order
}