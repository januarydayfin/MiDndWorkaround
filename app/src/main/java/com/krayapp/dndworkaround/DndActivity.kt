package com.krayapp.dndworkaround

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        setupModeSelection()
    }

    private fun openPermissionDialog() {
        permissionDialog = DndPermissionDialog(this, notificationRightsGranted())

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

        restoreModeUI()
    }

    private fun restoreModeUI() {
        when (prefs?.recordMode) {
            MODE_OFF -> vb.radioGroup.check(R.id.off)
            MODE_SILENT -> vb.radioGroup.check(R.id.silent)
            MODE_VIBRO -> vb.radioGroup.check(R.id.vibro)
            else -> {}
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

    private fun recordMode(mode: Int) {
        prefs?.recordMode = mode
    }
}