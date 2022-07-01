package com.example.onlineshop.ui.fragments.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.local.data_store.setting.SettingDataStore
import com.example.onlineshop.data.local.data_store.setting.SettingInfo
import com.example.onlineshop.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelSetting @Inject constructor(
    private val handler: NewsWorkerHandler,
    private val repository: ShopRepository,
    private val mainDataStore: MainDataStore,
    private val settingDataStore: SettingDataStore,
) : ViewModel() {

    suspend fun logOut(context: Context): Flow<Boolean> {
        return repository.logOut(context).map {
            if (it) {
                mainDataStore.updateHasBeenLoggedIn(false)
            }
            it
        }
    }

    fun settingFLow(): Flow<SettingInfo> {
        return settingDataStore.preferences
    }

    fun updateIndex(newIndex: Int) {
        viewModelScope.launch {
            settingDataStore.updateIndex(newIndex)
        }
    }

    fun cancel() {
        handler.cancel()
    }

    fun setup(periodicHour: Long, initialDelayMillisecond: Int) {
        handler.setup(
            periodicHour = periodicHour,
            initialDelayMillisecond = initialDelayMillisecond.toLong()
        )
    }
}
