package com.example.onlineshop.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val liveData = MutableLiveData<ConnectionState>()
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun observe(lifecycleOwner: LifecycleOwner, observer: Observer<ConnectionState>) {
        liveData.observe(lifecycleOwner, observer)
    }

    init {
        val request = createNetworkRequest()
        val callback = createCallback()
        connectivityManager.requestNetwork(
            request, callback
        )
        updateState()
    }

    private fun createNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }

    private fun createCallback(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                super.onUnavailable()
                liveData.postValue(ConnectionState.UNCONNECTED)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                liveData.postValue(ConnectionState.CONNECTED)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                liveData.postValue(ConnectionState.LOW_CONNECTION)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                liveData.postValue(ConnectionState.UNCONNECTED)
            }
        }
    }

    fun notifyWhenConnected(cb: () -> Unit) {
        liveData.observeOnce({ cb() }) {
            it == ConnectionState.CONNECTED
        }
    }

    private fun getState(): ConnectionState {
        return if (isNetworkAvailable()) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.UNCONNECTED
        }
    }

    private fun isNetworkAvailable(): Boolean {
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }

    fun updateState() {
        liveData.postValue(getState())
    }
}
