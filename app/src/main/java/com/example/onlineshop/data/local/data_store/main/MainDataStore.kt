package com.example.onlineshop.data.local.data_store.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val MAIN_DATA_STORE = "MAIN_DATA_STORE"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = MAIN_DATA_STORE)

@Singleton
class MainDataStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val dataStore = context.dataStore
    val preferences = dataStore.data.catch {

    }.map {

    }

    private object MainPreferencesKey {
        const val PASSWORD_KEY = "passwordKey"
        const val USERNAME_KEY = "usernameKey"
    }
}