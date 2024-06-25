package com.krayapp.dndworkaround

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.krayapp.dndworkaround.databinding.MainActivityBinding


class DndActivity : AppCompatActivity() {
    private lateinit var vb: MainActivityBinding

    private lateinit var notificationManager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = MainActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(vb.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        vb.letDnd.setOnClickListener { openDndSettings() }
        vb.letBattery.setOnClickListener { openAppSettings() }
        vb.letAutostart.setOnClickListener { openAutostart() }
    }


    private fun openDndSettings() {
        startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
    }

    private fun openAutostart() {
        startActivity(
            Intent().setComponent(
                ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
            )
        )
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }
}