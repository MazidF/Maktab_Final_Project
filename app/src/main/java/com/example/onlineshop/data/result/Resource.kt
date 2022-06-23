package com.example.onlineshop.data.result

import retrofit2.Response
import java.lang.Exception

sealed class Resource<T>( // Resource, (Result)
    protected val data: T? = null,
    protected val error: Throwable? = null,
    // message, network_exception
) {

    class Loading<R> : Resource<R>()
    class Reloading<R> : Resource<R>()
    class Success<R>(data: R) : Resource<R>(data = data) {
        fun body() = data!!
    }
    class Fail<R>(error: Throwable) : Resource<R>(error = error) {
        fun error() = error!!

        fun throwException() : Nothing {
            throw error()
        }
    }

    companion object {
        fun <P> loading() = Loading<P>()
        fun <P> reloading() = Reloading<P>()
        fun <P> success(data: P) = Success(data)
        fun <P> fail(error: Throwable) = Fail<P>(error)

        fun <P> fromResponse(response: Response<P>): Resource<P> {
            val body = response.body()
            return if (response.isSuccessful && body != null) {
                success(body)
            } else {
                fail(Exception(response.errorBody()?.string() ?: "Response data was null"))
            }
        }
    }

    fun asSuccess(): Success<T>? {
        if (isSuccessful) {
            return (this as Success)
        }
        return null
    }

    val isSuccessful = this is Success<*> && data != null

    fun <R> map(transformer: (T) -> R): Resource<R> {
        return when(this) {
            is Fail -> fail(error())
            is Loading -> loading()
            is Reloading -> reloading()
            is Success -> success(transformer(body()))
        }
    }
}
