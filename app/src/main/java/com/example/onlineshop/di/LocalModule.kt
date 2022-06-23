package com.example.onlineshop.di

import android.content.Context
import com.example.onlineshop.data.local.ILocalDataSource
import com.example.onlineshop.data.local.LocalCustomerDataSource
import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.di.qualifier.LocalCustomerDataStoreAnnotation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    @LocalCustomerDataStoreAnnotation
    fun provideLocalDataStore(
        @DispatcherIO dispatcher: CoroutineDispatcher,
        @ApplicationContext context: Context,
    ) : ILocalDataSource {
        return LocalCustomerDataSource(
            dispatcher = dispatcher,
            context = context,
        )
    }
}