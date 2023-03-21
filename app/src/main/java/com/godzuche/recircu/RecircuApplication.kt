package com.godzuche.recircu

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecircuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, BuildConfig.GCP_API_KEY)
    }
}