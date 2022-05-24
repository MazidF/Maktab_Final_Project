package com.example.onlineshop.utils

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

fun Fragment.launchOnState(state: Lifecycle.State, block: suspend () -> Unit) {
    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            block()
        }
    }
}

fun loadImageInto(url: String, imageView: ImageView) {
    Glide.with(imageView)
        .load(url)
        .into(imageView)
}