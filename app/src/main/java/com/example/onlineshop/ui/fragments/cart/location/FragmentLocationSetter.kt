package com.example.onlineshop.ui.fragments.cart.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentLocationSetterBinding
import com.example.onlineshop.ui.fragments.FragmentLoadable

class FragmentLocationSetter : FragmentLoadable(R.layout.fragment_location_setter, false) {

    private val viewModel: ViewModelLocationSetter by viewModels()

    private var _binding: FragmentLocationSetterBinding? = null
    private val binding: FragmentLocationSetterBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLocationSetterBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {

    }

    private fun observe() = with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun refresh() {
        // do nothing
    }
}