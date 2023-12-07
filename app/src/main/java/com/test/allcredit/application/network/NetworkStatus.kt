package com.test.allcredit.application.network

sealed class NetworkStatus {
    object Available : NetworkStatus()

    object Unavailable : NetworkStatus()
}