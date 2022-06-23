package com.example.onlineshop.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.utils.ConnectionState
import com.example.onlineshop.utils.NetworkManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class FragmentConnectionObserver(
    resource: Int,
) : Fragment(resource) {
    protected val navController by lazy {
        findNavController()
    }

    @Inject
    lateinit var networkManager: NetworkManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkManager.observe(viewLifecycleOwner) {
            when(it) {
                ConnectionState.UNCONNECTED -> {
                    navigateToConnectionFailed()
                }
            }
        }
    }

    abstract fun navigateToConnectionFailed()
}