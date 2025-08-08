package com.krayapp.dndworkaround

import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_ALARMS
import android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.google.android.play.core.review.testing.FakeReviewManager
import com.krayapp.dndworkaround.databinding.MainActivityBinding


class DndActivity : AppCompatActivity() {
    private lateinit var vb: MainActivityBinding
    private var permissionDialog: DndPermissionDialog? = null
    private lateinit var notificationManager: NotificationManager
    private var prefs: GlobalPrefs? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.removeSystemInsets()
        vb = MainActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(vb.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        vb.letPermission.setOnClickListener { openPermissionDialog() }
        vb.aboutApp.setOnClickListener { showAboutBottomsheet() }
        vb.githubLink.setOnClickListener { openGitPage() }
        setupModeSelection()

    }

    private fun openPermissionDialog() {
        permissionDialog = DndPermissionDialog(this) { notificationRightsGranted() }

        with(permissionDialog!!) {
            setOnDismissListener { permissionDialog = null }
            show()
        }
    }

    private fun showAboutBottomsheet() {
        AboutBottomSheet().show(supportFragmentManager, "")
    }

    override fun onResume() {
        super.onResume()
        if (!notificationRightsGranted())
            openPermissionDialog()
        permissionDialog?.updatePermissionViews()
        restoreModeUI()
        showInAppPreviewWindow()
    }

    private fun restoreModeUI() {
        when (prefs?.recordMode) {
            MODE_OFF -> vb.radioGroup.check(R.id.off)
            MODE_SILENT -> vb.radioGroup.check(R.id.silent)
            MODE_VIBRO -> vb.radioGroup.check(R.id.vibro)
            else -> {}
        }
    }

    private fun showInAppPreviewWindow() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(this,reviewInfo)
            }
        }
    }
    private fun notificationRightsGranted(): Boolean {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        return manager.isNotificationPolicyAccessGranted()
    }

    private fun setupModeSelection() {
        prefs = GlobalPrefs(this)
        vb.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.off -> recordMode(MODE_OFF)
                R.id.vibro -> recordMode(MODE_VIBRO)
                R.id.silent -> recordMode(MODE_SILENT)
            }
        }
    }

    private fun openGitPage() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://github.com/januarydayfin/MiDndWorkaround")
        )
        startActivity(browserIntent)
    }

    private fun recordMode(mode: Int) {
        prefs?.recordMode = mode
        tryToApplyMode(mode)
    }

    private fun tryToApplyMode(mode: Int) {
        try {
            val dnd = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (dnd.currentInterruptionFilter == INTERRUPTION_FILTER_PRIORITY || dnd.currentInterruptionFilter == INTERRUPTION_FILTER_ALARMS) {
                when (mode) {
                    MODE_OFF -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                    MODE_SILENT -> notificationManager.setInterruptionFilter(
                        INTERRUPTION_FILTER_ALARMS
                    ) //needs for
                    MODE_VIBRO -> audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                }
            } else
                Toast.makeText(this, R.string.apply_on_next_dnd, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
            Toast.makeText(this, R.string.permission_error, Toast.LENGTH_SHORT).show()
        }

    }
}