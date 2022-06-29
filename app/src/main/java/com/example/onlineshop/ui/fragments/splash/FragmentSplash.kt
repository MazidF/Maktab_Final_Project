package com.example.onlineshop.ui.fragments.splash

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.work.*
import com.example.onlineshop.R
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.databinding.AlertDialogBinding
import com.example.onlineshop.databinding.FragmentSplashBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.fragments.cart.main.ViewModelCart
import com.example.onlineshop.ui.fragments.login.ViewModelLogin
import com.example.onlineshop.utils.failedDialog
import com.example.onlineshop.utils.hideBackground
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.woker.SetupWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSplash : FragmentConnectionObserver(R.layout.fragment_splash) {

    @Inject
    lateinit var mainDataStore: MainDataStore
    private val viewModel: ViewModelLogin by activityViewModels()

    private var _binding: FragmentSplashBinding? = null
    private val binding: FragmentSplashBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSplashBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {

    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            mainDataStore.preferences.collect { info ->
                if (info.hasValidId()) {
                    login(info.customerId)
                } else {
                    startWorker()
                }
            }
        }
    }

    private fun login(customerId: Long) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.login(customerId).collect {
                if (it.not()) {
                    showDialog {
                        login(customerId)
                    }
                } else {
                    navigate()
                }
            }
        }
    }

    private fun navigate() {
        navController.navigate(
            FragmentSplashDirections.actionFragmentSplashToFragmentHome()
        )
    }

    private fun startWorker() {
        val request = OneTimeWorkRequestBuilder<SetupWorker>().build()
        val workManager = WorkManager.getInstance(requireContext())

        workManager.enqueue(request)
        workManager.getWorkInfoByIdLiveData(request.id)
            .observe(viewLifecycleOwner) {
                if (it.state.isFinished) {
                    if (it.state == WorkInfo.State.FAILED) {
                        showDialog {
                            startWorker()
                        }
                    } else if (it.state == WorkInfo.State.SUCCEEDED) {
                        navigate()
                    }
                }
            }
    }

    private fun showDialog(block: () -> Unit) {
        errorDialog = failedDialog(block, this::back)
        errorDialog!!.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(
            FragmentSplashDirections.actionFragmentSplashToFragmentNetworkConnectionFailed()
        )
    }

}