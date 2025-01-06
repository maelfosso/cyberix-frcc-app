package com.cyberix.beac.frc

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class FRCApplication : Application() {
    lateinit var appConfig: AppConfig // Make it accessible throughout the app

    override fun onCreate() {
        super.onCreate()

        // Load config in a coroutine to avoid blocking the main thread
        CoroutineScope(Dispatchers.IO).launch {
            appConfig = loadConfig(applicationContext, BuildConfig.CONFIG_FILE) ?: AppConfig(
                "http://10.0.2.2:8080/",
            )
        }
    }

    companion object {
        fun getAppConfig(context: Context): AppConfig? {
            return (context.applicationContext as? FRCApplication)?.appConfig
        }
    }
}