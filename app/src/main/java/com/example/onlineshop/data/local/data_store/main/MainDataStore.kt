package com.example.onlineshop.data.local.data_store.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.onlineshop.di.qualifier.DispatcherIO
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val MAIN_DATA_STORE = "MAIN_DATA_STORE"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = MAIN_DATA_STORE)

@Singleton
class MainDataStore @Inject constructor(
    @ApplicationContext context: Context,
    @DispatcherIO dispatcher: CoroutineDispatcher,
) {
    private val dataStore = context.dataStore
    val preferences = dataStore.data.catch {

    }.map { preference ->
        val userId = preference[MainPreferencesKey.CUSTOMER_ID_KEY] ?: -1
        MainInfo(
            userId = userId,
        )
    }.flowOn(dispatcher)

    suspend fun updateCustomerId(userId: Long) {
        dataStore.edit {
            it[MainPreferencesKey.CUSTOMER_ID_KEY] = userId
        }
    }

    private object MainPreferencesKey {
        val CUSTOMER_ID_KEY = longPreferencesKey(name = "customerIdKey")
    }
}