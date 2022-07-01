package com.example.onlineshop.ui.fragments.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.onlineshop.R
import com.example.onlineshop.data.local.data_store.setting.SettingDataStore
import com.example.onlineshop.databinding.FragmentSettingBinding
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.widgit.CheckableItemView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSetting : Fragment(R.layout.fragment_setting) {

    private val viewModel: ViewModelSetting by viewModels()
    private var selectedIndex: Int = -1
    private lateinit var list: List<CheckableItemView>

    private var _binding: FragmentSettingBinding? = null
    private val binding: FragmentSettingBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        list = fragmentSettingWorkerRoot.children.mapIndexed { i, item ->
            (item as CheckableItemView).apply {
                setOnClickListener {
                    changeIndex(i)
                }
            }
        }.toList()
        fragmentSettingLogout.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.logOut(requireContext()).collect {
                if (it) {
                    navigateToHome()
                } else {
                    Toast.makeText(requireContext(), "خروج نا موفق بود.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(
            FragmentSettingDirections.actionFragmentSettingToFragmentHome()
        )
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.settingFLow().collect { info ->
                val index = if (info.workerHasBeenSetup()) {
                    info.workerDurationIndex
                } else {
                    0
                }.also {
                    selectedIndex = it
                }
                setupWorkerRoot(index, info.workerHasBeenSetup())
            }
        }
    }

    private fun setupWorkerRoot(index: Int, hasBeenSetup: Boolean) = with(binding) {
        fragmentSettingWorker.apply {
            setOnClickListener {
                val isChecked = fragmentSettingWorker.isChecked() == true
                fragmentSettingWorkerRoot.isVisible = isChecked
            }
            setIsChecked(hasBeenSetup)
        }

        changeIndex(index)
    }

    fun changeIndex(newIndex: Int) {
        list[selectedIndex].setIsChecked(false)
        selectedIndex = newIndex
        list[selectedIndex].setIsChecked(true)
    }

    private fun updateIndex(newIndex: Int) {
        viewModel.updateIndex(newIndex)
    }

    override fun onStop() {
        super.onStop()
        if (binding.fragmentSettingWorker.isChecked() == true) {
            updateIndex(selectedIndex)
            viewModel.setup((selectedIndex + 1) * 4L, 20_000)
        } else {
            updateIndex(-1)
            viewModel.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
