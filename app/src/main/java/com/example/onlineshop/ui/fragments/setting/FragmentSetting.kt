package com.example.onlineshop.ui.fragments.setting

import android.os.Bundle
import android.view.View
import androidx.core.view.allViews
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.onlineshop.R
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.local.data_store.setting.SettingDataStore
import com.example.onlineshop.databinding.FragmentSettingBinding
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.widgit.CheckableItemView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSetting(
) : Fragment(R.layout.fragment_setting) {

    private var selectedIndex: Int = -1
    private var _binding: FragmentSettingBinding? = null
    private val binding: FragmentSettingBinding
        get() = _binding!!

    @Inject
    lateinit var workerHandler: NewsWorkerHandler
    @Inject
    lateinit var settingDataStore: SettingDataStore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {

    }

    private fun setupWorkerRoot(index: Int) = with(binding) {
        fragmentSettingWorker.apply {
            setOnClickListener {
                val isChecked = fragmentSettingWorker.isChecked() == true
                fragmentSettingWorkerRoot.isVisible = isChecked
            }
            if (index != -1) {
                callOnClick()
            }
        }
        selectedIndex = if (index != -1) index else 0
        val list = fragmentSettingWorkerRoot.children.map {
            it as CheckableItemView
        }.toList()
        fun changeIndex(newIndex: Int) {
            list[selectedIndex].setIsChecked(false)
            selectedIndex = newIndex
            list[selectedIndex].setIsChecked(true)
        }
        list.forEachIndexed { i, it ->
            it.setOnClickListener {
                changeIndex(i)
            }
        }
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            settingDataStore.preferences.collect {
                setupWorkerRoot(it.workerDurationIndex)
            }
        }
    }

    private fun updateIndex(newIndex: Int) {
        lifecycleScope.launch {
            settingDataStore.updateIndex(newIndex)
        }
    }

    override fun onDestroyView() {
        if (binding.fragmentSettingWorker.isChecked() == true) {
            updateIndex(selectedIndex)
            workerHandler.setup((selectedIndex + 1) * 4L)
        } else {
            updateIndex(-1)
            workerHandler.cancel()
        }
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
