package com.example.onlineshop.data.repository

import androidx.paging.PagingData
import com.example.onlineshop.data.local.ILocalDataStore
import com.example.onlineshop.data.model.*
import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.model.customer.RawCustomer
import com.example.onlineshop.data.remote.RemoteCustomerDataSource
import com.example.onlineshop.data.remote.RemoteProductDataSource
import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.utils.logger
import com.example.onlineshop.utils.result.SafeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRepository(
    private val remoteProduct: RemoteProductDataSource,
    private val remoteCustomer: RemoteCustomerDataSource,
    private val local: ILocalDataStore,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
) {
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

    private suspend fun <T> load(
        reload: Boolean,
        fakeData: T? = null,
        block: suspend () -> SafeApiCall<T>
    ): Flow<SafeApiCall<T>> = flow<SafeApiCall<T>> {
        if (reload) {
            emit(SafeApiCall.reloading())
        } else {
            emit(SafeApiCall.loading())
        }
        fakeData?.let {
            emit(SafeApiCall.success(it))
        }
        emit(block())
    }.catch { cause ->
        emit(SafeApiCall.fail(cause))
    }.flowOn(dispatcher)

    suspend fun getCategories(reload: Boolean): Flow<SafeApiCall<List<Category>>> {
        return load(reload) {
            remoteProduct.getCategories(1, 100)
        }
    }

    suspend fun getCategoriesByParentId(
        parentId: Long,
        reload: Boolean
    ): Flow<SafeApiCall<List<Category>>> {
        return load(reload) {
            remoteProduct.getCategoriesByParentId(parentId, 1, 100)
        }
    }

    suspend fun getMostPopularProduct(
        size: Int,
        reload: Boolean
    ): Flow<SafeApiCall<List<Product>>> {
        return load(reload) {
            remoteProduct.getMostPopularProduct(1, size)
        }
    }

    suspend fun getMostRatedProduct(size: Int, reload: Boolean): Flow<SafeApiCall<List<Product>>> {
        return load(reload) {
            remoteProduct.getMostRatedProduct(1, size)
        }
    }

    suspend fun getNewestProduct(size: Int, reload: Boolean): Flow<SafeApiCall<List<Product>>> {
        return load(reload) {
            remoteProduct.getNewestProduct(1, size)
        }
    }

    suspend fun getProductInfo(productId: Long): Flow<SafeApiCall<ProductInfo>> {
        return load(false) {
            remoteProduct.getProductInfo(productId)
        }
    }

    suspend fun getProductById(ids: Array<Long>, fake: Boolean = true): Flow<SafeApiCall<List<Product>>> {
        val seed = System.currentTimeMillis()
        return if (fake) {
            load(false, List(ids.size) {
                Product.fake(seed + it)
            }) {
                remoteProduct.getProductById(ids)
            }
        } else {
            load(false) {
                val a = remoteProduct.getProductById(ids)
                val t = a
                a
            }
        }
    }

    //////////////////////////////////// customers /////////////////////////////////////

    suspend fun getCustomerById(userId: Long, reload: Boolean): Flow<SafeApiCall<Customer>> {
        return load(reload) {
            remoteCustomer.getCustomerById(userId)
        }
    }

    suspend fun signIn(email: String, password: String): Flow<SafeApiCall<Customer>> {
        return load(false) {
            val raw = RawCustomer(
                email = email,
                password = password
            )
            remoteCustomer.signIn(raw)
        }
    }

    suspend fun logIn(email: String, password: String): Flow<SafeApiCall<Customer>> {
        return load(false) {
            val customer = remoteCustomer.getCustomerByEmail(email)
            if (customer.asSuccess()!!.body().password == password) {
                customer
            } else {
                throw Exception("Email or Password was wrong!!")
            }
        }
    }

    suspend fun logIn(customerId: Long): Flow<SafeApiCall<Customer>> {
        return load(false) {
            remoteCustomer.logIn(customerId)
        }
    }

    /////////////////////////// local //////////////////////////

    suspend fun loadCartMap(): HashMap<Long, Int> {
        return local.loadCartMap()
    }

    suspend fun saveCartMap(map: HashMap<Long, Int>) {
        return local.saveCartMap(map)
    }

    suspend fun getMainPosterProducts(): SafeApiCall<ProductImages> {
        return remoteProduct.getMainPosterProducts()
    }
}