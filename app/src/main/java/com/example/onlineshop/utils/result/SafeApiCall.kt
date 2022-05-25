package com.example.onlineshop.utils.result

import retrofit2.Response
import java.lang.Exception

sealed class SafeApiCall<T>(
    protected val data: T? = null,
    protected val error: Throwable? = null,
) {

    class Loading<R> : SafeApiCall<R>()
    class Reloading<R> : SafeApiCall<R>()
    class Success<R>(data: R) : SafeApiCall<R>(data = data) {
        fun body() = data!!
    }
    class Fail<R>(error: Throwable) : SafeApiCall<R>(error = error) {
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

        fun <P> fromResponse(response: Response<P>): SafeApiCall<P> {
            val body = response.body()
            return if (response.isSuccessful && body != null) {
                success(body)
            } else {
                fail(Exception(response.errorBody()?.string() ?: "Response data was null"))
            }
        }
    }

    val isSuccessful = this is Success<*> && data != null

    fun <R> map(transformer: (T) -> R): SafeApiCall<R> {
        return when(this) {
            is Fail -> fail(error())
            is Loading -> loading()
            is Reloading -> reloading()
            is Success -> success(transformer(body()))
        }
    }
}
