package com.siddhartho.trendingrepositories.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build

class CheckNetworkConnection(private val context: Context) {

    fun registerNetworkCallback() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    NetworkConnection.isConnected = true
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    NetworkConnection.isConnected = false
                }
            })
        }
    }
}