package com.krayapp.dndworkaround

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

/**
 * Фоновый сервис (Foreground Service), предназначенный для отслеживания изменений режима «Не беспокоить» (DND).
 *
 * Сервис работает в фоновом режиме для обеспечения непрерывного мониторинга системного фильтра прерываний.
 * При получении системного события [NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED],
 * он перенаправляет интент в [DndReceiver] для дальнейшей обработки логики приложения.
 *
 * Для работы в фоновом режиме на Android 8.0 и выше создается уведомление с низким приоритетом.
 */
class DndService : Service() {
    companion object {
        private const val CHANNEL_ID = "DndServiceChannel"
        private const val NOTIFICATION_ID = 1

        const val CUSTOM_INTERRUPTION_ACTION = "krayapp.dnd.INTERRUPTION_FILTER"
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent?.action == "android.app.action.INTERRUPTION_FILTER_CHANGED"
                && intent.`package` == "com.krayapp.dndfixer"
            ) {
                val intent = Intent(context, DndReceiver::class.java).apply {
                    action = CUSTOM_INTERRUPTION_ACTION
                }
                sendBroadcast(intent)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = createNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        registerReceiver()
        return START_STICKY
    }

    private fun registerReceiver() {
        runCatching {
            ContextCompat.registerReceiver(
                this,
                receiver,
                IntentFilter().apply {
                    addAction("android.app.action.INTERRUPTION_FILTER_CHANGED")
                },
                ContextCompat.RECEIVER_EXPORTED
            )
        }
    }

    private fun unregisterReceiver() {
        runCatching {
            this.unregisterReceiver(receiver)
        }
    }

    override fun onDestroy() {
        unregisterReceiver()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.service_title))
            .setContentText(getString(R.string.service_text))
            .setOngoing(true)
            .setSmallIcon(R.drawable.main_logo)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "DND Service Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}