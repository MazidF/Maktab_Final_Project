package com.example.onlineshop.data.remote

import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.model.customer.RawCustomer
import com.example.onlineshop.data.remote.api.CustomerApi
import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.utils.asSafeApiCall
import com.example.onlineshop.utils.result.SafeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import javax.inject.Inject

class RemoteCustomerDataSource @Inject constructor(
    private val api: CustomerApi,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
) {

    suspend fun getCustomerById(userId: Long): SafeApiCall<Customer> {
        return api.getCustomer(userId).asSafeApiCall()
    }

    suspend fun getCustomerByEmail(userEmail: String): SafeApiCall<Customer> {
        return api.getCustomer(userEmail).asSafeApiCall()
    }

    suspend fun signIn(rawCustomer: RawCustomer): SafeApiCall<Customer> {
        return api.createCustomer(rawCustomer).asSafeApiCall()
    }

    suspend fun logIn(customerId: Long): SafeApiCall<Customer> {
        return api.getCustomer(customerId).asSafeApiCall()
    }
}