package com.app.flixtrain.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A utility class to monitor network connectivity.
 */
@Singleton
class NetworkMonitor @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {
    val isOnline: Flow<Boolean> = callbackFlow {
        /**
         * Checks the current network status.
         */
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                if (capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                    launch { send(true) }
                }
            }

            override fun onLost(network: Network) {
                launch { send(false) }
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                // This is called when network capabilities change (e.g., losing internet capability)
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    launch { send(true) }
                } else {
                    launch { send(false) }
                }
            }

            // Called when a network is blocked or unblocked (e.g., VPN state changes)
            override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                if (capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true && !blocked) {
                    launch { send(true) }
                } else {
                    launch { send(false) }
                }
            }
        }

        // Register the network callback
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)

        // Emit initial state
        val initialNetwork = connectivityManager.activeNetwork
        val initialCapabilities = connectivityManager.getNetworkCapabilities(initialNetwork)
        val initialOnline =
            initialCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        launch { send(initialOnline) }

        // When the collector cancels, unregister the callback
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged() // Ensure only distinct values are emitted
}