package com.example.onlineshop.utils.result

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
    }

    companion object {
        fun <P> loading() = Loading<P>()
        fun <P> reloading() = Reloading<P>()
        fun <P> success(data: P) = Success(data)
        fun <P> fail(error: Throwable) = Fail<P>(error)
    }

    val isSuccessful = this is Success<*> && data != null
}
