package com.krayapp.dndworkaround

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log

class DndReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        when (manager.currentInterruptionFilter) {
            NotificationManager.INTERRUPTION_FILTER_ALL -> enableSound(context)
            NotificationManager.INTERRUPTION_FILTER_PRIORITY -> muteSound(context)
            else -> {}
        }
    }


    private fun enableSound(context: Context?) {
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.setInterruptionFilter(
            NotificationManager.INTERRUPTION_FILTER_ALL
        )
    }

    private fun muteSound(context: Context?) {
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.setInterruptionFilter(
            NotificationManager.INTERRUPTION_FILTER_ALARMS
        )
    }
}