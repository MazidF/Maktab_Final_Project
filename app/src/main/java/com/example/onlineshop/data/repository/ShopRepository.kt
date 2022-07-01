package com.example.onlineshop.data.repository

import android.content.Context
import androidx.paging.PagingData
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.model.*
import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.model.customer.CustomerInfo
import com.example.onlineshop.data.model.customer.CustomerMetaData
import com.example.onlineshop.data.model.customer.RawCustomer
import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.data.model.order.OrderStatus
import com.example.onlineshop.data.model.order.SimpleOrder
import com.example.onlineshop.data.remote.RemoteCustomerDataSource
import com.example.onlineshop.data.remote.RemoteProductDataSource
import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.ui.model.LineItemWithImage
import com.example.onlineshop.ui.model.OrderItem
import com.example.onlineshop.utils.getDeviceId
import com.example.onlineshop.utils.toSimpleOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

class ShopRepository(
    private val remoteProduct: RemoteProductDataSource,
    private val remoteCustomer: RemoteCustomerDataSource,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val mainDataStore: MainDataStore,
) {
    lateinit var customer: Customer
        private set
    lateinit var currentOrder: OrderItem
        private set

    suspend fun getProductByDate(date: String): Resource<List<Product>> {
        return remoteProduct.getProductByDate(date)
    }

    fun getMostRatedProduct(): Flow<PagingData<Product>> {
        return remoteProduct.getMostRatedProduct()
    }

    fun getNewestProduct(): Flow<PagingData<Product>> {
        return remoteProduct.getNewestProduct()
    }

    fun getMostPopularProduct(): Flow<PagingData<Product>> {
        return remoteProduct.getMostPopularProduct()
    }

    fun getProductsByCategory(categoryId: String): Flow<PagingData<Product>> {
        return remoteProduct.getProductsByCategory(
            categoryId = categoryId,
        )
    }

    fun search(query: String): Flow<PagingData<ProductSearchItem>> {
        return remoteProduct.search(query = query)
    }

    // TODO: move to utils
    private fun <T> safeApiCall( //safe_api_call
        reload: Boolean,
        fakeData: T? = null,
        block: suspend () -> Resource<T>
    ): Flow<Resource<T>> = flow<Resource<T>> {
        fakeData?.let {
            emit(Resource.success(it))
        }
        emit(block())
    }.catch { cause ->
        emit(Resource.fail(cause))
    }.onStart {
        if (reload) {
            emit(Resource.reloading())
        } else {
            emit(Resource.loading())
        }
    }.flowOn(dispatcher)

    fun getCategories(reload: Boolean): Flow<Resource<List<Category>>> {
        return safeApiCall(reload) {
            remoteProduct.getCategories(1, 100)
        }
    }

    fun getCategoriesByParentId(
        parentId: Long,
        reload: Boolean
    ): Flow<Resource<List<Category>>> {
        return safeApiCall(reload) {
            remoteProduct.getCategoriesByParentId(parentId, 1, 100)
        }
    }

    fun getMostPopularProduct(
        size: Int,
        reload: Boolean
    ): Flow<Resource<List<Product>>> {
        return safeApiCall(reload) {
            remoteProduct.getMostPopularProduct(1, size)
        }
    }

    fun getMostRatedProduct(size: Int, reload: Boolean): Flow<Resource<List<Product>>> {
        return safeApiCall(reload) {
            remoteProduct.getMostRatedProduct(1, size)
        }
    }

    fun getNewestProduct(size: Int, reload: Boolean): Flow<Resource<List<Product>>> {
        return safeApiCall(reload) {
            remoteProduct.getNewestProduct(1, size)
        }
    }

    fun getProductInfo(productId: Long): Flow<Resource<ProductInfo>> {
        return safeApiCall(false) {
            remoteProduct.getProductInfo(productId)
        }
    }

    fun getProductById(
        ids: Array<Long>,
        fake: Boolean = true
    ): Flow<Resource<List<Product>>> {
        val seed = System.currentTimeMillis()
        return if (fake) {
            safeApiCall(false, List(ids.size) {
                Product.fake(seed + it)
            }) {
                remoteProduct.getProductById(ids)
            }
        } else {
            safeApiCall(false) {
                remoteProduct.getProductById(ids)
            }
        }
    }

    fun createReview(review: ProductReview): Flow<Resource<ProductReview>> {
        return safeApiCall(false) {
            remoteProduct.createReview(review)
        }
    }

    fun getReview(reviewId: Long): Flow<Resource<ProductReview>> {
        return safeApiCall(false) {
            remoteProduct.getReview(reviewId)
        }
    }

    fun getReviewOfProduct(
        productId: Long,
        size: Int,
    ): Flow<Resource<List<ProductReview>>> {
        val seed = System.currentTimeMillis()
        return safeApiCall(false, fakeData = List(size) {
            ProductReview.fake(seed + it)
        }) {
            remoteProduct.getReviewOfProduct(
                productId, size
            )
        }
    }

    fun getReviewOfProduct(
        productId: Long,
    ): Flow<PagingData<ProductReview>> {
        return remoteProduct.getReviewOfProduct(productId)
    }

    //////////////////////////////////// customers /////////////////////////////////////

    fun getCoupon(code: String): Flow<Resource<Coupon?>> {
        return safeApiCall(false) {
            remoteCustomer.getCoupon(code)
        }
    }

    fun getCustomerById(userId: Long, reload: Boolean): Flow<Resource<Customer>> {
        return safeApiCall(reload) {
            remoteCustomer.getCustomerById(userId)
        }
    }

    fun getCustomerInfo(userId: Long): Flow<Resource<CustomerInfo>> {
        return safeApiCall(false) {
            remoteCustomer.getCustomerInfo(userId)
        }
    }

    suspend fun updateCustomer(customerId: Long, customer: RawCustomer): Resource<Customer> {
        return remoteCustomer.updateCustomer(customerId, customer)
    }

    suspend fun signIn(email: String, password: String): Flow<Boolean> {
        val flow = safeApiCall(false) {
            val raw = RawCustomer(
                email = email,
                password = password,
                metaData = arrayListOf(),
            )
            remoteCustomer.signIn(raw)
        }
        return collectCustomerFlow(flow)
    }

    suspend fun logIn(email: String, password: String): Flow<Boolean> {
        val flow = safeApiCall(false) {
            val customer = remoteCustomer.getCustomerByEmail(email)
            if (customer.asSuccess()!!.body().password == password) {
                customer
            } else {
                throw Exception("Email or Password was wrong!!")
            }
        }
        return collectCustomerFlow(flow)
    }

    suspend fun logIn(customerId: Long): Flow<Boolean> {
        val flow = safeApiCall(false) {
            remoteCustomer.logIn(customerId)
        }
        return collectCustomerFlow(flow)
    }

    suspend fun logOut(context: Context): Flow<Boolean> {
        val deviceId = getDeviceId(context)
        return logIn(deviceId, deviceId)
    }

    private suspend fun collectCustomerFlow(
        flow: Flow<Resource<Customer>>,
    ): Flow<Boolean> {
        return flow {
            flow.collect {
                when (it) {
                    is Resource.Fail -> {
                        emit(false)
                    }
                    is Resource.Success -> {
                        val customer = it.body()
                        this@ShopRepository.customer = customer
                        val result = createOrderOrGetCurrent(customer.id, customer.currentOrderId)
                        mainDataStore.updateCustomerId(customer.id)
                        emit(result.isSuccessful)
                    }
                }
            }
        }.catch { cause ->
            val error = cause
            emit(false)
        }.flowOn(dispatcher)
    }

    suspend fun getOrder(orderId: Long): Resource<Order> {
        return remoteCustomer.getOrder(orderId)
    }

    private suspend fun createOrderOrGetCurrent(customerId: Long, currentOrderId: Long): Resource<OrderItem> {
        val orderResult = if (currentOrderId == -1L) {
            remoteCustomer.createOrder(customerId)
        } else {
            getOrder(currentOrderId)
        }

        val order = if (orderResult is Resource.Success) {
            orderResult.body()
        } else {
            return orderResult.map {
                OrderItem.fromOrder(it, listOf())
            }
        }

        val orderItemResult = orderToOrderItem(listOf(order)).map {
            it[0]
        }

        val customerResult = if (orderItemResult is Resource.Success) {
            currentOrder = orderItemResult.body()
            val metaData = arrayListOf(
                CustomerMetaData(
                    CustomerInfo.CURRENT_ORDER_ID_KEY,
                    currentOrder.id.toString(),
                )
            )
            val rawCustomer = RawCustomer.from(
                customer, metaData
            )
            updateCustomer(customerId, rawCustomer)
        } else {
            return orderItemResult
        }

        customer =  if (customerResult is Resource.Success) {
            customerResult.body()
        } else {
            return Resource.fail(Exception("Unable to update the customer"))
        }

        return orderItemResult

/*        val ordersResult = remoteCustomer.getPendingOrders(customerId)
        return if (ordersResult is Resource.Success) {
            val orders = ordersResult.body()
            val order = if (orders.isEmpty()) {
                val orderResult = remoteCustomer.createOrder(customer.id)
                if (orderResult is Resource.Success) {
                    orderResult.body()
                } else {
                    return orderResult.map {
                        OrderItem.fromOrder(it, listOf())
                    }
                }
            } else {
                orders[0]
            }
            val orderItemResult = orderToOrderItem(listOf(order)).map {
                it[0]
            }
            return if (orderItemResult is Resource.Success) {
                val orderItem = orderItemResult.body()
                val metaData = arrayListOf(
                    CustomerMetaData(
                        CustomerInfo.CURRENT_ORDER_ID_KEY,
                        orderItem.id.toString(),
                    )
                )
                val rawCustomer = RawCustomer.from(
                    customer, metaData
                )
                val customerResult = updateCustomer(customerId, rawCustomer)
                if (customerResult is Resource.Success) {
                    orderItemResult
                } else {
                    return Resource.fail(java.lang.Exception("Unable to update customer"))
                }
            } else {
                orderItemResult
            }
        } else {
            ordersResult.map {
                OrderItem.fromOrder(it[0], listOf())
            }
        }*/

    }

    private suspend fun orderToOrderItem(orders: List<Order>): Resource<List<OrderItem>> {
        val list = orders.flatMap {
            it.lineItems.map { li ->
                li.productId
            }
        }
        val response = remoteProduct.getProductById(list.toTypedArray())
        val product = response.asSuccess()?.body()
            ?: run {
                return Resource.fail(Exception("Unable to load Product list!!!"))
            }
        val orderItems = orders.map { order ->
            OrderItem.fromOrder(
                order,
                order.lineItems.map { lineItem ->
                    LineItemWithImage(
                        lineItem, product.firstOrNull {
                            it.id == lineItem.productId
                        } ?: Product.fromLineItem(lineItem)
                    )
                }
            )
        }
        return Resource.success(orderItems)
    }

    fun getPendingOrder(customerId: Long): Flow<Resource<OrderItem>> {
        return safeApiCall(false) {
            createOrderOrGetCurrent(customerId, customer.currentOrderId)
        }
    }

    fun completeOrder(coupon: Coupon?, customerNote: String): Flow<Boolean> {
        val newOrder = currentOrder.toSimpleOrder(
            coupons = coupon?.let { arrayOf(it) } ?: arrayOf(),
            note = customerNote,
            status = OrderStatus.COMPLETED,
        )
        return flow {
            var response = updateOrder(newOrder).asSuccess()
            if (response is Resource.Success) {
                response = createOrderOrGetCurrent(customer.id, customer.currentOrderId).asSuccess()
                if (response is Resource.Success) {
                    emit(true)
                } else {
                    emit(false)
                }
            } else {
                emit(false)
            }
        }
    }

    suspend fun updateOrder(order: SimpleOrder): Resource<OrderItem> {
        val result = remoteCustomer.updateOrder(order)
        return result.asSuccess()?.body()?.let {
            orderToOrderItem(listOf(it)).map { list ->
                list[0]
            }.also { orderItem ->
                if (orderItem is Resource.Success) {
                    currentOrder = orderItem.body()
                }
            }
        } ?: result.map {
            OrderItem.fromOrder(it, listOf())
        }
    }

    fun getOrders(customerId: Long, status: OrderStatus): Flow<PagingData<OrderItem>> {
        return remoteCustomer.getOrders(customerId, status, this::orderToOrderItem)
    }

    fun getOrders(customerId: Long): Flow<Resource<List<Order>>> {
        return safeApiCall(false) {
            remoteCustomer.getOrders(customerId)
        }
    }

    suspend fun getMainPosterProducts(): Resource<ProductImages> {
        return remoteProduct.getMainPosterProducts()
    }
}
