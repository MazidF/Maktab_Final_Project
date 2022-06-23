package com.example.onlineshop.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.result.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class RetriableViewModel : ViewModel() {

    private val failedStack by lazy {
        Stack<Job>()
    }

    fun <T> launch(
        flow: Flow<Resource<T>>,
        collector: (Resource<T>) -> Unit,
    ) {
        val job = viewModelScope.launch {
            flow.collect {
                collector(it)
            }
        }
    }
}