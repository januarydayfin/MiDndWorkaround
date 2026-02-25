package com.krayapp.dndworkaround

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.krayapp.dndworkaround.components.DndMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFS_KEY = "dndprefskey"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_KEY)


private object PreferenceKeys {
    val MODE_KEY = stringPreferencesKey("dndmodekey")
    val BACKGROUND_WORK_TYPE = intPreferencesKey("backgroundworktypekey")
}

fun Context.recordMode(): Flow<String> = this.dataStore.data.map { preferences ->
    preferences[PreferenceKeys.MODE_KEY] ?: DndMode.OFF.toString()
}


suspend fun Context.setRecordMode(mode: String) {
    dataStore.edit { settings ->
        settings[PreferenceKeys.MODE_KEY] = mode
    }
}

fun Context.backgroundWorkType(): Flow<Int> = this.dataStore.data.map { preferences ->
    preferences[PreferenceKeys.BACKGROUND_WORK_TYPE] ?: ECO
}

suspend fun Context.setBackgroundWorkType(type: Int) {
    dataStore.edit { settings ->
        settings[PreferenceKeys.BACKGROUND_WORK_TYPE] = type
    }
}