package com.test.allcredit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AllCreditApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}