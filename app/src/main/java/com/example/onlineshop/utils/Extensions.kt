package com.example.onlineshop.utils

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.onlineshop.R
import com.example.onlineshop.utils.result.SafeApiCall
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.function.Predicate

fun Fragment.launchOnState(state: Lifecycle.State, block: suspend () -> Unit): Job {
    return lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            block()
        }
    }
}

fun loadImageInto(
    url: Any,
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

fun <K, V> HashMap<K, V>.getWithDefault(key: K, default: V): V {
    return this[key] ?: default.also {
        this[key] = it
    }
}

fun View.setMargin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
    layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
        this.setMargins(left, top, right, bottom)
    }
}

fun TextView.setHtmlText(html: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(html)
    }
}

fun <T> LiveData<T>.observeOnce(observer: Observer<T>, filter: (T) -> Boolean = { true }) {
    var cb: Observer<T>? = null
    cb = Observer {
        if (filter(it)) {
            observer.onChanged(it)
            this.removeObserver(cb!!)
        }
    }
    observeForever(cb)
}

fun TextView.onEnteredKeyPressed(cb: (text: String, actionId: Int, event: KeyEvent) -> Boolean) {
    setOnEditorActionListener { _, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val text = text.toString().trim()
            cb(text, actionId, event)
        } else {
            false
        }
    }
}
