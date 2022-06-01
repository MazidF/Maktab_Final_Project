package com.example.onlineshop.ui.fragments.profile

import android.os.Bundle
import android.view.View
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentProfileBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentProfile : FragmentConnectionObserver(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

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

    override fun navigateToConnectionFailed() {
        navController.navigate(FragmentProfileDirections.actionFragmentProfileToFragmentNetworkConnectionFailed())
    }
}