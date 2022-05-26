package com.example.onlineshop.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.onlineshop.R
import com.example.onlineshop.utils.result.SafeApiCall
import kotlinx.coroutines.launch
import retrofit2.Response

fun Fragment.launchOnState(state: Lifecycle.State, block: suspend () -> Unit) {
    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            block()
        }
    }
}

fun loadImageInto(
    url: String,
    imageView: ImageView,
    option: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable> = { this }
) {
    Glide.with(imageView)
        .applyDefaultRequestOptions(glideDiskCacheStrategy)
        .load(url)
        .error(R.drawable.ic_product_default)
        .option()
        .into(imageView)
}

fun Any.clazz() = this::class.java

inline fun <reified T> List<T>.insertFooter(t: T): List<T> {
    return listOf(t, *this.toTypedArray())
}

inline fun <reified T> List<T>.insertHeader(t: T): List<T> {
    return listOf(*this.toTypedArray(), t)
}

fun <T> Response<T>.asSafeApiCall(): SafeApiCall<T> {
    return SafeApiCall.fromResponse(this)
}