package com.krayapp.dndworkaround

import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors

class DndApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}