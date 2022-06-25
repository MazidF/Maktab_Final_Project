package com.example.onlineshop.data.repository

import androidx.paging.PagingData
import com.example.onlineshop.data.local.ILocalDataSource
import com.example.onlineshop.data.model.*
import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.model.customer.RawCustomer
import com.example.onlineshop.data.model.order.Order
import com.example.onlineshop.data.model.order.SimpleOrder
import com.example.onlineshop.data.remote.RemoteCustomerDataSource
import com.example.onlineshop.data.remote.RemoteProductDataSource
import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.ui.model.LineItemWithImage
import com.example.onlineshop.ui.model.OrderItem
import com.example.onlineshop.woker.SetupWorker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import java.lang.IndexOutOfBoundsException

class ShopRepository(
    private val remoteProduct: RemoteProductDataSource,
    private val remoteCustomer: RemoteCustomerDataSource,
    private val local: ILocalDataSource,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
) {
    lateinit var customer: Customer
        private set

    fun setupCustomer(worker: SetupWorker, customer: Customer) {
        this.customer = customer
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
    private suspend fun <T> safeApiCall( //safe_api_call
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

    suspend fun getCategories(reload: Boolean): Flow<Resource<List<Category>>> {
        return safeApiCall(reload) {
            remoteProduct.getCategories(1, 100)
        }
    }

    suspend fun getCategoriesByParentId(
        parentId: Long,
        reload: Boolean
    ): Flow<Resource<List<Category>>> {
        return safeApiCall(reload) {
            remoteProduct.getCategoriesByParentId(parentId, 1, 100)
        }
    }

    suspend fun getMostPopularProduct(
        size: Int,
        reload: Boolean
    ): Flow<Resource<List<Product>>> {
        return safeApiCall(reload) {
            remoteProduct.getMostPopularProduct(1, size)
        }
    }

    suspend fun getMostRatedProduct(size: Int, reload: Boolean): Flow<Resource<List<Product>>> {
        return safeApiCall(reload) {
            remoteProduct.getMostRatedProduct(1, size)
        }
    }

    suspend fun getNewestProduct(size: Int, reload: Boolean): Flow<Resource<List<Product>>> {
        return safeApiCall(reload) {
            remoteProduct.getNewestProduct(1, size)
        }
    }

    suspend fun getProductInfo(productId: Long): Flow<Resource<ProductInfo>> {
        return safeApiCall(false) {
            remoteProduct.getProductInfo(productId)
        }
    }

    suspend fun getProductById(
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

    //////////////////////////////////// customers /////////////////////////////////////

    suspend fun getCustomerById(userId: Long, reload: Boolean): Flow<Resource<Customer>> {
        return safeApiCall(reload) {
            remoteCustomer.getCustomerById(userId)
        }
    }

    suspend fun signIn(email: String, password: String): Flow<Resource<Customer>> {
        return safeApiCall(false) {
            val raw = RawCustomer(
                email = email,
                password = password
            )
            remoteCustomer.signIn(raw)
        }
    }

    suspend fun logIn(email: String, password: String): Flow<Resource<Customer>> {
        return safeApiCall(false) {
            val customer = remoteCustomer.getCustomerByEmail(email)
            if (customer.asSuccess()!!.body().password == password) {
                customer
            } else {
                throw Exception("Email or Password was wrong!!")
            }
        }
    }

    suspend fun logIn(customerId: Long): Flow<Resource<Customer>> {
        return safeApiCall(false) {
            remoteCustomer.logIn(customerId)
        }
    }

    private suspend fun createOrderOrGetCurrent(customerId: Long): Resource<OrderItem> {
        val result = remoteCustomer.getPendingOrders(customerId)
        return if (result is Resource.Success) {
            val orders = result.body()
            if (orders.isEmpty()) {
                Resource.fail(IndexOutOfBoundsException())
            } else {
                val order = orders[0]
                return orderToOrderItem(order)
            }
        } else {
            result.map {
                OrderItem.fromOrder(it[0], listOf())
            }
        }
    }

    private suspend fun orderToOrderItem(order: Order): Resource<OrderItem> {
        val list = order.lineItems
        val ids = list.map {
            it.productId
        }
        val product = remoteProduct.getProductById(ids.toTypedArray()).asSuccess()?.body()
            ?: return Resource.fail(Exception("Unable to load Product list!!!"))
        val orderItem = OrderItem.fromOrder(
            order, list.map { lineItem ->
                LineItemWithImage(
                    lineItem,
                    product.firstOrNull {
                        it.id == lineItem.productId
                    } ?: Product.fromLineItem(lineItem)
                )
            }
        )
        return Resource.success(orderItem)
    }

    suspend fun getPendingOrder(customerId: Long): Flow<Resource<OrderItem>> {
        return safeApiCall(false) {
            createOrderOrGetCurrent(customerId)
        }
    }

    suspend fun updateOrder(order: SimpleOrder): Resource<OrderItem> {
        val result = remoteCustomer.updateOrder(order)
        return result.asSuccess()?.body()?.let {
            orderToOrderItem(it)
        } ?: result.map {
            OrderItem.fromOrder(it, listOf())
        }
    }

    fun getFinishedOrders(customerId: Long): Flow<PagingData<Order>> {
        return remoteCustomer.getFinishedOrders(customerId)
    }

    /////////////////////////// local //////////////////////////

    suspend fun loadCartMap(): HashMap<Long, Int> {
        return local.loadCartMap()
    }

    suspend fun saveCartMap(map: HashMap<Long, Int>) {
        return local.saveCartMap(map)
    }

    suspend fun getMainPosterProducts(): Resource<ProductImages> {
        return remoteProduct.getMainPosterProducts()
    }
}
