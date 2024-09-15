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

class DndReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        when (manager.currentInterruptionFilter) {
            INTERRUPTION_FILTER_PRIORITY -> shutUp(context)
            INTERRUPTION_FILTER_ALL -> unmuteNotification(context)
            else -> {}
        }
    }


    private fun shutUp(context: Context?) {
        val audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mode = GlobalPrefs(context).recordMode

        when (mode) {
            MODE_OFF -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            MODE_SILENT -> notificationManager.setInterruptionFilter(INTERRUPTION_FILTER_ALARMS) //needs for
            MODE_VIBRO -> audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
        }

    }

    private fun unmuteNotification(context: Context?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.setInterruptionFilter(INTERRUPTION_FILTER_ALL)
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
    }


}