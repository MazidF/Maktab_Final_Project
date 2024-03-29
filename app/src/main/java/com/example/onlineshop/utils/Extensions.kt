package com.example.onlineshop.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.provider.Settings.Secure
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.onlineshop.R
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.databinding.AlertDialogBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


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

fun <T> Response<T>.asResource(): Resource<T> {
    return Resource.fromResponse(this)
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

fun <T> LiveData<T>.observeOnce(filter: (T) -> Boolean = { true }, observer: Observer<T>) {
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

fun <T> readObjectFromFile(fileName: String, root: File): /*HashMap<Long, Int>*/ T? {
    val file = File(root, fileName)
    if (file.exists().not()) {
        return null
    }
    return ObjectInputStream(file.inputStream()).use {
        it.readUnshared() as? T
    }
}

fun writeObjectOnFile(any: Any, fileName: String, root: File) {
    val file = File(root, fileName)
    if (file.exists()) {
        file.delete()
    } else {
        file.createNewFile()
    }
    ObjectOutputStream(file.outputStream()).use {
        it.writeUnshared(any)
        it.flush()
    }
}

fun logger(msg: String) {
    Log.d("online_shop_logger", msg)
}

@SuppressLint("ClickableViewAccessibility")
fun ViewPager2.autoScroll(interval: Long) {
    val handler = Handler()
    var scrollPosition = 0
    val runnable = object : Runnable {
        override fun run() {
            /**
             * Calculate "scroll position" with
             * adapter pages count and current
             * value of scrollPosition.
             */
            val count = adapter?.itemCount ?: 0
            setCurrentItem(scrollPosition++ % count, true)

            handler.postDelayed(this, interval)
        }
    }

    registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            // Updating "scroll position" when user scrolls manually
            scrollPosition = position + 1
        }

        override fun onPageScrollStateChanged(state: Int) {
            // Not necessary
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            // Not necessary
        }
    })

    handler.post(runnable)
    setOnTouchListener { _, motionEvent ->
        when(motionEvent.action) {
            KeyEvent.ACTION_DOWN -> {
                handler.removeCallbacks(runnable)
                true
            }
            KeyEvent.ACTION_UP -> {
                handler.post(runnable)
                true
            }
            else -> false
        }
    }
}

@SuppressLint("HardwareIds")
fun getDeviceId(context: Context): String {
    return /*Secure.getString(
        context.contentResolver,
        Secure.ANDROID_ID
    )*/ "mazid_test_4"
}

fun AlertDialog.hideBackground() = apply {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}

fun Fragment.failedDialog(positiveBlock: () -> Unit, negativeBlock: () -> Unit): AlertDialog {
    var dialog: AlertDialog? = null
    val binding = AlertDialogBinding.inflate(layoutInflater).apply {
        alertDialogYes.setOnClickListener {
            positiveBlock()
            dialog!!.hide()
        }
        alertDialogNo.setOnClickListener {
            negativeBlock()
            dialog!!.hide()
        }
    }
    dialog = AlertDialog.Builder(requireContext())
        .setView(binding.root)
        .setCancelable(false)
        .create()
        .hideBackground()
    return dialog
}

@SuppressLint("SimpleDateFormat")
fun Date.toISO8601(): String {
    val tz: TimeZone = TimeZone.getTimeZone("UTC")
    val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
    df.timeZone = tz
    return df.format(this)
}

fun RadioButton.setup(defaultValue: Boolean = false, block: (Boolean) -> Unit = {}) {
    isSelected = defaultValue
    isChecked = defaultValue
    setOnClickListener {
        if (isSelected) {
            isSelected = false
            isChecked = false
        } else {
            isSelected = true
            isChecked = true
        }
        block(isChecked)
    }
}
