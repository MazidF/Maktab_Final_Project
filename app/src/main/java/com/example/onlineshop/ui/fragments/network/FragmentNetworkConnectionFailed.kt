package com.example.onlineshop.ui.fragments.network

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentNetworkConnectionFailedBinding
import com.example.onlineshop.utils.ConnectionState
import com.example.onlineshop.utils.NetworkManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentNetworkConnectionFailed : Fragment(R.layout.fragment_network_connection_failed) {
    private var _binding: FragmentNetworkConnectionFailedBinding? = null
    private val binding: FragmentNetworkConnectionFailedBinding
        get() = _binding!!

    @Inject
    lateinit var networkManager: NetworkManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNetworkConnectionFailedBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        fragmentConnectionRetry.setOnClickListener {
            networkManager.updateState()
        }
    }

    private fun observe() {
        networkManager.observe(viewLifecycleOwner) {
            when(it) {
                ConnectionState.CONNECTED, ConnectionState.LOW_CONNECTION -> {
                    back()
                }
                ConnectionState.UNCONNECTED -> {
                    // do nothing
                }
            }
        }
    }

    private fun back() {
        (requireActivity() as AppCompatActivity).onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}