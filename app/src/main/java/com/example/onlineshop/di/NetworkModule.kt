package com.example.onlineshop.di

import com.example.onlineshop.data.model.*
import com.example.onlineshop.data.remote.api.CustomerApi
import com.example.onlineshop.data.remote.api.ProductApi
import com.example.onlineshop.data.remote.api.deserializer.*
import com.example.onlineshop.utils.BASE_URL
import com.example.onlineshop.utils.CONSUMER_KEY
import com.example.onlineshop.utils.CONSUMER_SECRET
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSource
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
            try {
                it.proceed(newRequest)
            } catch (e: Exception) {
                okhttp3.Response.Builder()
                    .request(newRequest)
                    .protocol(Protocol.HTTP_1_1)
                    .code(987)
                    .message(e.message ?: "message")
                    .body(ResponseBody.create(null, "{$e}"))
                    .build()
            }
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
            .registerTypeAdapter(ProductImages::class.java, ProductImagesDeserializer)
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