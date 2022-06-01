package com.example.onlineshop.di

import com.example.onlineshop.data.model.Category
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.data.model.ProductInfo
import com.example.onlineshop.data.model.ProductSearchItem
import com.example.onlineshop.data.remote.api.CustomerApi
import com.example.onlineshop.data.remote.api.ProductApi
import com.example.onlineshop.data.remote.api.deserializer.CategoryDeserializer
import com.example.onlineshop.data.remote.api.deserializer.ProductDeserializer
import com.example.onlineshop.data.remote.api.deserializer.ProductInfoDeserializer
import com.example.onlineshop.data.remote.api.deserializer.ProductSearchItemDeserializer
import com.example.onlineshop.utils.BASE_URL
import com.example.onlineshop.utils.CONSUMER_KEY
import com.example.onlineshop.utils.CONSUMER_SECRET
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggerInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideInterceptor() : Interceptor {
        return Interceptor {
            val oldRequest = it.request()
            val newUrl = oldRequest.url().newBuilder()
                .addQueryParameter("consumer_key", CONSUMER_KEY)
                .addQueryParameter("consumer_secret", CONSUMER_SECRET)
                .build()
            val newRequest = oldRequest.newBuilder()
                .url(newUrl)
                .build()
            it.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: Interceptor,
        loggerInterceptor: HttpLoggingInterceptor,
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(loggerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson() : Gson {
        return GsonBuilder()
            .registerTypeAdapter(Product::class.java, ProductDeserializer)
            .registerTypeAdapter(Category::class.java, CategoryDeserializer)
            .registerTypeAdapter(ProductInfo::class.java, ProductInfoDeserializer)
            .registerTypeAdapter(ProductSearchItem::class.java, ProductSearchItemDeserializer)
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        gson: Gson,
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideProductApi(
        retrofit: Retrofit,
    ) : ProductApi {
        return retrofit.create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCustomerApi(
        retrofit: Retrofit,
    ) : CustomerApi {
        return retrofit.create(CustomerApi::class.java)
    }

}