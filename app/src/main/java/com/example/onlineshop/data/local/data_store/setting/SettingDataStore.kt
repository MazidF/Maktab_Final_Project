package com.example.onlineshop.data.local.data_store.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DATA_STORE_SETTING = "DATA_STORE_SETTING"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_SETTING)

@Singleton
class SettingDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    val preferences = dataStore.data.catch { cause ->
        // TODO: handle cause
    }.map {

    }

    private object SettingKey {
        val keyTheme = stringPreferencesKey("KEY_THEME")
    }

}