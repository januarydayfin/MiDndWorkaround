package com.krayapp.dndworkaround

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.review.ReviewManagerFactory
import com.gun0912.tedpermission.coroutine.TedPermission
import com.krayapp.dndworkaround.components.backgroundRememberedType
import com.krayapp.dndworkaround.data.setBackgroundWorkType
import com.krayapp.dndworkaround.mvi.BackgroundWorkType
import com.krayapp.dndworkaround.screen.MainScreen
import com.krayapp.dndworkaround.theme.AppTheme
import kotlinx.coroutines.launch


class DndActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            AppTheme {
                MainScreen(
                    enableForegroundService = {
                        enableForegroundService()
                    },
                    disableForegroundService = {
                        disableForegroundService()
                    }
                )
            }
        }

    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            if (backgroundRememberedType() == BackgroundWorkType.SERVICE)
                enableForegroundService()
        }
        showInAppPreviewWindow()
    }

    private fun showInAppPreviewWindow() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(this, reviewInfo)
            }
        }
    }

    private fun enableForegroundService() {
        lifecycleScope.launch {
            val permissionResult =
                TedPermission.create()
                    .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
                    .check()

            if (permissionResult.isGranted)
                startForegroundService(Intent(this@DndActivity, DndService::class.java))
            else
                this@DndActivity.setBackgroundWorkType(BackgroundWorkType.RECEIVER.toString())
        }

    }

    private fun disableForegroundService() {
        val intent = Intent(this, DndService::class.java)
        stopService(intent)
    }

}