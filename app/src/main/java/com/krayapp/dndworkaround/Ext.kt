package com.krayapp.dndworkaround

import android.content.res.Configuration
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Window.removeSystemInsets() {
    val decorView = decorView
    statusBarColor = decorView.resources.getColor(R.color.transparent, null)
    decorView.systemUiVisibility = 0

    WindowInsetsControllerCompat(this, decorView).isAppearanceLightNavigationBars = !isNightMode()
    WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars = !isNightMode()

    ViewCompat.setOnApplyWindowInsetsListener(decorView) { _, insets ->

        ViewCompat.onApplyWindowInsets(
            decorView,
            insets.replaceSystemWindowInsets(0, 0, 0, 0)
        )
    }
}

fun Window.isNightMode(): Boolean {
    return when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_YES -> true
        Configuration.UI_MODE_NIGHT_NO -> false
        else -> false
    }
}