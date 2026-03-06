package com.krayapp.dndworkaround

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_ALARMS
import android.app.NotificationManager.INTERRUPTION_FILTER_ALL
import android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import com.krayapp.dndworkaround.DndService.Companion.CUSTOM_INTERRUPTION_ACTION
import com.krayapp.dndworkaround.components.backgroundRememberedType
import com.krayapp.dndworkaround.data.backgroundWorkType
import com.krayapp.dndworkaround.data.dndMode
import com.krayapp.dndworkaround.mvi.BackgroundWorkType
import com.krayapp.dndworkaround.mvi.DndMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DndReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        scope.launch {
            val isServiceEvent = context.backgroundRememberedType() == BackgroundWorkType.SERVICE
                    && intent?.action == CUSTOM_INTERRUPTION_ACTION

            val isBroadcastEvent = context.backgroundRememberedType() == BackgroundWorkType.RECEIVER
                    && intent?.action == "android.app.action.INTERRUPTION_FILTER_CHANGED"

            if (isServiceEvent || isBroadcastEvent)
                runCatching {
                    when (manager.currentInterruptionFilter) {
                        INTERRUPTION_FILTER_PRIORITY -> shutUp(context)
                        INTERRUPTION_FILTER_ALL -> unmuteNotification(context)
                        else -> {}
                    }
                }
        }
    }


    private suspend fun shutUp(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mode = DndMode.valueOf(context.dndMode().first())

        when (mode) {
            DndMode.OFF -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            DndMode.SILENT -> notificationManager.setInterruptionFilter(INTERRUPTION_FILTER_ALARMS)
            DndMode.VIBRO -> audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
        }

    }

    private fun unmuteNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.setInterruptionFilter(INTERRUPTION_FILTER_ALL)
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
    }
}
