package com.example.onlineshop.widgit

interface Refreshable<T> : Bindable<List<T>> {
    val refreshableId: Int

    fun refresh(list: List<T>)
}