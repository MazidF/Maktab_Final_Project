package com.example.onlineshop.data.repository

import androidx.paging.PagingData
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.model.*
import com.example.onlineshop.data.model.customer.Customer
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import retrofit2.Response

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

    fun getCustomerById(userId: Long, reload: Boolean): Flow<Resource<Customer>> {
        return safeApiCall(reload) {
            remoteCustomer.getCustomerById(userId)
        }
    }

    suspend fun signIn(email: String, password: String): Flow<Boolean> {
        val flow = safeApiCall(false) {
            val raw = RawCustomer(
                email = email,
                password = password
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

    private suspend fun collectCustomerFlow(flow: Flow<Resource<Customer>>): Flow<Boolean> {
        return flow {
            flow.collect {
                when (it) {
                    is Resource.Fail -> {
                        val error = it.error()
                        emit(false)
                    }
                    is Resource.Success -> {
                        val customer = it.body()
                        this@ShopRepository.customer = customer
                        val result = createOrderOrGetCurrent(customer.id)
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

    private suspend fun createOrderOrGetCurrent(customerId: Long): Resource<OrderItem> {
        val result = remoteCustomer.getPendingOrders(customerId)
        return if (result is Resource.Success) {
            val orders = result.body()
            val order = if (orders.isEmpty()) {
                val order = remoteCustomer.createOrder(customer.id)
                if (order is Resource.Success) {
                    order.body()
                } else {
                    return order.map {
                        OrderItem.fromOrder(it, listOf())
                    }
                }
            } else {
                orders[0]
            }
            return orderToOrderItem(listOf(order)).map {
                it[0]
            }.also {
                if (it is Resource.Success) {
                    currentOrder = it.body()
                }
            }
        } else {
            result.map {
                OrderItem.fromOrder(it[0], listOf())
            }
        }
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
            createOrderOrGetCurrent(customerId)
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
