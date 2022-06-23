package com.example.onlineshop.di

import com.example.onlineshop.data.local.ILocalDataSource
import com.example.onlineshop.data.remote.RemoteCustomerDataSource
import com.example.onlineshop.data.remote.RemoteProductDataSource
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.di.qualifier.LocalCustomerDataStoreAnnotation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        remoteProduct: RemoteProductDataSource,
        remoteCustomer: RemoteCustomerDataSource,
        @LocalCustomerDataStoreAnnotation local: ILocalDataSource,
        @DispatcherIO dispatcher: CoroutineDispatcher,
    ) : ProductRepository {
        return ProductRepository(
            remoteProduct = remoteProduct,
            remoteCustomer = remoteCustomer,
            local = local,
            dispatcher = dispatcher,
        )
    }
}
