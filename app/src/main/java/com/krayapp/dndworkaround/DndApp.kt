package com.krayapp.dndworkaround

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.krayapp.dndworkaround.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DndApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        startKoin {
            androidLogger()
            androidContext(this@DndApp)
            modules(appModule)
        }
    }
}