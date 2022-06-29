package com.example.onlineshop.data.local.data_store.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.onlineshop.di.qualifier.DispatcherIO
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DATA_STORE_SETTING = "DATA_STORE_SETTING"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_SETTING)

@Singleton
class SettingDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    @DispatcherIO dispatcher: CoroutineDispatcher
) {

    private val dataStore = context.dataStore

    val preferences = dataStore.data.catch { cause ->
        // TODO: handle cause
    }.map { preference ->
        val workerDurationIndex = preference[SettingKey.WORKER_DURATION_INDEX_KEY] ?: -1

        SettingInfo(
            workerDurationIndex = workerDurationIndex,
        )
    }.flowOn(dispatcher)

    suspend fun updateIndex(newIndex: Int) {
        dataStore.edit {
            it[SettingKey.WORKER_DURATION_INDEX_KEY] = newIndex
        }
    }

    private object SettingKey {
        val THEME_KEY = stringPreferencesKey("themeKey")
        val WORKER_DURATION_INDEX_KEY = intPreferencesKey("workerDurationIndexKey")
    }

}