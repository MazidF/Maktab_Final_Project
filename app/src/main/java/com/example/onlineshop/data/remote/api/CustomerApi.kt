package com.example.onlineshop.data.remote.api

import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.model.customer.RawCustomer
import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.data.model.order.OrderStatus
import com.example.onlineshop.data.model.order.SimpleOrder
import retrofit2.Response
import retrofit2.http.*

interface CustomerApi {

    @POST("customers")
    suspend fun createCustomer(
        @Body customer: RawCustomer,
    ) : Response<Customer>

    @PUT("customers/{id}")
    suspend fun updateCustomer(
        @Path("id") id: Long,
        @Body customer: Customer,
    ) : Response<Customer>

    @GET("customers/{id}")
    suspend fun getCustomer(
        @Path("id") id: Long,
    ) : Response<Customer>

    @GET("customers")
    suspend fun getCustomer(
        @Query("email") email: String,
    ) : Response<Customer>

    @POST("orders")
    suspend fun createOrder(
        @Body customer: Customer,
    ) : Response<Order>

    @PUT("orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Long,
        @Body order: SimpleOrder,
    ) : Response<Order>

    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") id: Long,
    ) : Response<Order>

    @GET("orders")
    suspend fun getOrders(
        @Query("customer") customerId: Long,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("orderby") orderBy: String = "data",
        @Query("status") status: String = OrderStatus.ANY.value,
    ) : Response<List<Order>>
}