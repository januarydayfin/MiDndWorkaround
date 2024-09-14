package com.krayapp.dndworkaround

import android.content.Context
import android.content.SharedPreferences

class GlobalPrefs(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    var recordMode: Int
        get() {
            return getMode()
        }
        set(value) {
            recordMode(value)
        }

    var useContacts: Boolean
        get() {
            return isUseContacts()
        }
        set(value) {
            setContactsUse(value)
        }


    private fun recordMode(mode: Int) {
        prefs.edit().putInt(MODE_KEY, mode).apply()
    }

    private fun getMode(): Int = prefs.getInt(MODE_KEY, 0)


    private fun isUseContacts() : Boolean = prefs.getBoolean(CONTACT_KEY, false)

    private fun setContactsUse(use: Boolean) {
        prefs.edit().putBoolean(CONTACT_KEY, use).apply()
    }

    companion object {
        private const val PREFS_KEY = "dndprefskey"

        private const val MODE_KEY = "dndmodekey"
        private const val CONTACT_KEY = "contactUseKey"
    }
}