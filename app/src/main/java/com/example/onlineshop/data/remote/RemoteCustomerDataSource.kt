package com.example.onlineshop.data.remote

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.model.customer.RawCustomer
import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.data.model.order.OrderStatus
import com.example.onlineshop.data.model.order.SimpleOrder
import com.example.onlineshop.data.remote.api.CustomerApi
import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.utils.asResource
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.ui.model.OrderItem
import com.example.onlineshop.utils.INITIAL_SIZE
import com.example.onlineshop.utils.PAGE_SIZE
import com.example.onlineshop.utils.PRE_FETCH_DISTANCE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class RemoteCustomerDataSource @Inject constructor(
    private val api: CustomerApi,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
) {

    private val pagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        prefetchDistance = PRE_FETCH_DISTANCE,
        enablePlaceholders = true, // TODO: add shimmer
        initialLoadSize = INITIAL_SIZE,
    )

    suspend fun getCustomerById(userId: Long): Resource<Customer> {
        return api.getCustomer(userId).asResource()
    }

    suspend fun getCustomerByEmail(userEmail: String): Resource<Customer> {
        return api.getCustomer(userEmail).asResource()
    }

    suspend fun signIn(rawCustomer: RawCustomer): Resource<Customer> {
        return api.createCustomer(rawCustomer).asResource()
    }

    suspend fun logIn(customerId: Long): Resource<Customer> {
        return api.getCustomer(customerId).asResource()
    }

    suspend fun updateOrder(order: SimpleOrder): Resource<Order> {
        return api.updateOrder(order.id, order).asResource()
    }

    suspend fun getPendingOrders(customerId: Long): Resource<List<Order>> {
        return getCurrentOrder(
            customerId,
        ).asResource()
    }

    private suspend fun getCurrentOrder(
        id: Long,
    ) : Response<List<Order>> {
        return api.getOrders(
            customerId = id, 1, 1,
            OrderStatus.PROCESSING.value,
        )
    }

    fun getFinishedOrders(customerId: Long): Flow<PagingData<Order>> {
        return RemotePagingSource.getPager(config = pagingConfig) { page, perPage ->
            api.getOrders(
                customerId, page, perPage,
                status = OrderStatus.COMPLETED.value,
            ).asResource()
        }.flow.flowOn(dispatcher)
    }
}