package com.krayapp.dndworkaround

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

class DndReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        when (manager.currentInterruptionFilter) {
            NotificationManager.INTERRUPTION_FILTER_PRIORITY -> shutUp(context)
            NotificationManager.INTERRUPTION_FILTER_ALL -> unmuteNotification(context)
            else -> {}
        }
    }


    private fun shutUp(context: Context?) {
        val audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val mode = GlobalPrefs(context).getMode()

        audioManager.ringerMode = when (mode) {
            MODE_OFF -> AudioManager.RINGER_MODE_NORMAL
            MODE_SILENT -> AudioManager.RINGER_MODE_SILENT
            MODE_VIBRO -> AudioManager.RINGER_MODE_VIBRATE
            else -> AudioManager.RINGER_MODE_NORMAL
        }

    }

    private fun unmuteNotification(context: Context?) {
        val audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
    }
}