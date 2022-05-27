package com.example.onlineshop.ui.fragments.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentCartBinding

class FragmentCart : Fragment(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)

        init()
        observe()
    }

    private fun init() = with(binding) {

    }

    private fun observe() = with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}