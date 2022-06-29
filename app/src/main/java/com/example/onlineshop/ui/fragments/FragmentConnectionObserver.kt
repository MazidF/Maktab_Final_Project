package com.example.onlineshop.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
    protected var errorDialog: AlertDialog? = null

    protected fun back() {
        (requireActivity() as? AppCompatActivity)?.onBackPressed()
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

    override fun onStop() {
        errorDialog?.hide()
        errorDialog = null
        super.onStop()
    }

    abstract fun navigateToConnectionFailed()
}