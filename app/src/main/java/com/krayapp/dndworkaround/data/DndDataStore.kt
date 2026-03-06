package com.krayapp.dndworkaround.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.krayapp.dndworkaround.mvi.DndMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFS_KEY = "dndprefskey"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_KEY)


private object PreferenceKeys {
    val MODE_KEY = stringPreferencesKey("dndmodekey")
    val BACKGROUND_WORK_TYPE = stringPreferencesKey("backgroundworktypekey")
}

fun Context.dndMode(): Flow<String> = this.dataStore.data.map { preferences ->
    preferences[PreferenceKeys.MODE_KEY] ?: DndMode.OFF.toString()
}


suspend fun Context.setDndMode(mode: String) {
    dataStore.edit { settings ->
        settings[PreferenceKeys.MODE_KEY] = mode
    }
}

fun Context.backgroundWorkType(): Flow<String> = this.dataStore.data.map { preferences ->
    preferences[PreferenceKeys.BACKGROUND_WORK_TYPE] ?: "RECEIVER"
}

suspend fun Context.setBackgroundWorkType(type: String) {
    dataStore.edit { settings ->
        settings[PreferenceKeys.BACKGROUND_WORK_TYPE] = type
    }
}