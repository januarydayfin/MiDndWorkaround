package com.krayapp.dndworkaround.components

import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_ALARMS
import android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY
import android.content.ComponentName
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.AudioManager
import android.provider.Settings
import com.krayapp.dndworkaround.data.backgroundWorkType
import com.krayapp.dndworkaround.mvi.BackgroundWorkType
import com.krayapp.dndworkaround.mvi.DndMode
import kotlinx.coroutines.flow.first

/**
 * На лету применяет выбранный режим приложения
 */
fun Context.tryToApplyMode(mode: DndMode) {
    runCatching {
        val dnd = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (dnd.currentInterruptionFilter == INTERRUPTION_FILTER_PRIORITY || dnd.currentInterruptionFilter == INTERRUPTION_FILTER_ALARMS) {
            when (mode) {
                DndMode.OFF -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                DndMode.SILENT -> notificationManager.setInterruptionFilter(
                    INTERRUPTION_FILTER_ALARMS
                ) //needs for
                DndMode.VIBRO -> audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            }
        }
    }
}

/**
 * Открывает активити для настройки автостарта в Xiaomi
 */
fun Context.openAutostart() {
    runCatching {
        startActivity(
            Intent().setComponent(
                ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
            )
        )
    }
}

/**
 * Открывает настройки доступа к днд
 */
fun Context.openDndSettings() {
    startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
}

fun Context.dndGranted(): Boolean {
    val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    return manager.isNotificationPolicyAccessGranted
}

suspend fun Context.backgroundRememberedType(): BackgroundWorkType {
    return BackgroundWorkType.valueOf(backgroundWorkType().first())
}