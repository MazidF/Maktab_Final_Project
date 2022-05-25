package com.example.onlineshop.widgit

interface Refreshable<T> {

    fun provider() : List<T>

    fun refresh(list: List<T>)
}