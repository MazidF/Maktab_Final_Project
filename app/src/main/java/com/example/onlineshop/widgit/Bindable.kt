package com.example.onlineshop.widgit

import android.view.View

interface Bindable<T> {
    fun bind(t: T?)

    fun getView(): View
}