package com.example.onlineshop.di

import com.example.onlineshop.data.remote.api.RemoteProductDataSource
import com.example.onlineshop.data.repository.ProductRepository
import com.example.onlineshop.di.qualifier.DispatcherIO
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
        remote: RemoteProductDataSource,
        @DispatcherIO dispatcher: CoroutineDispatcher,
    ) : ProductRepository {
        return ProductRepository(
            remote = remote,
            dispatcher = dispatcher,
        )
    }
}
