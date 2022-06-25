package com.example.onlineshop.ui.fragments.cart.location

import androidx.lifecycle.ViewModel
import com.example.onlineshop.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelLocationSetter @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {



}
