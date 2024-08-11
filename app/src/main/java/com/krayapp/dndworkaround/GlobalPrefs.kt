package com.krayapp.dndworkaround

import android.content.Context
import android.content.SharedPreferences

class GlobalPrefs(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    fun recordMode(mode: Int) {
        prefs.edit().putInt(MODE_KEY, mode).apply()
    }

    fun getMode(): Int = prefs.getInt(MODE_KEY, 0)


    companion object {
        private const val PREFS_KEY = "dndprefskey"

        private const val MODE_KEY = "dndmodekey"
    }
}