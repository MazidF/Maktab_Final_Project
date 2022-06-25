package com.example.onlineshop.ui.fragments.splash

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.work.*
import com.example.onlineshop.R
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.databinding.FragmentSplashBinding
import com.example.onlineshop.ui.MainActivity
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.utils.observeOnce
import com.example.onlineshop.woker.SetupWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSplash : FragmentConnectionObserver(R.layout.fragment_splash) {

    @Inject
    lateinit var mainDataStore: MainDataStore

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
                if (info.hasBeenLoggedIn()) {
                    navigate()
                } else {
                    startWorker()
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
                        showDialog()
                    }
                }
            }
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("تنظیمات با شکست مواجه شد!!")
            .setMessage("دوباره تکرار کنیم؟")
            .setPositiveButton("بله") { _, _ ->
                startWorker()
            }
            .setNegativeButton("نه") { _, _ ->
                (requireActivity() as? AppCompatActivity)?.onBackPressed()
            }
            .setCancelable(false)
            .create()
        dialog.show()
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