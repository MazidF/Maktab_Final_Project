package com.example.onlineshop.ui.fragments

import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.widgit.Bindable

open class BindableHolder<T>(
    private val bindable: Bindable<T>
)  : RecyclerView.ViewHolder(bindable.getView()) {
    fun bind(item: T?) {
        bindable.bind(item)
    }
}