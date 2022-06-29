package com.example.onlineshop.di

import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.remote.RemoteCustomerDataSource
import com.example.onlineshop.data.remote.RemoteProductDataSource
import com.example.onlineshop.data.repository.ShopRepository
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
        @DispatcherIO dispatcher: CoroutineDispatcher,
        mainDataStore: MainDataStore,
    ): ShopRepository {
        return ShopRepository(
            remoteProduct = remoteProduct,
            remoteCustomer = remoteCustomer,
            dispatcher = dispatcher,
            mainDataStore = mainDataStore,
        )
    }
}
