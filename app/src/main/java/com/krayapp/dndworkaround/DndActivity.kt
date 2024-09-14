package com.krayapp.dndworkaround

import android.Manifest.permission.READ_CONTACTS
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.krayapp.dndworkaround.databinding.MainActivityBinding


class DndActivity : AppCompatActivity() {
    private lateinit var vb: MainActivityBinding
    private var permissionDialog: DndPermissionDialog? = null
    private lateinit var notificationManager: NotificationManager
    private var prefs: GlobalPrefs? = null
    private val starredChecker = StarredChecker(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.removeSystemInsets()
        vb = MainActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(vb.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        vb.letPermission.setOnClickListener { openPermissionDialog() }
        setupModeSelection()

        vb.settings.setOnClickListener { openPermissionDialog() }
        setupContactSwitch()
    }

    private fun setupContactSwitch() {
        vb.favoritesContactsAvailable.setOnCheckedChangeListener { _, isChecked ->
            prefs?.useContacts = isChecked
        }
        vb.favoritesContactsAvailable.setOnClickListener {
            letContactPermission { granted ->
                if (granted)
                    starredChecker.getStarredContactNumbers()

                vb.favoritesContactsAvailable.isChecked = granted
            }
        }
        vb.favoritesContactsAvailable.isChecked = prefs?.useContacts ?: false
    }

    private fun openPermissionDialog() {
        permissionDialog = DndPermissionDialog(this, notificationRightsGranted())

        with(permissionDialog!!) {
            setOnDismissListener { permissionDialog = null }
            show()
        }
    }

    override fun onResume() {
        super.onResume()
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

    private fun letContactPermission(onPermission: (Boolean) -> Unit) {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    onPermission(true)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    onPermission(false)
                }
            })
            .setPermissions(READ_CONTACTS)
            .check()
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
        applyRingerMode(mode)
    }

    private fun applyRingerMode(mode: Int) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.ringerMode = when (mode) {
            MODE_OFF -> AudioManager.RINGER_MODE_NORMAL
            MODE_SILENT -> AudioManager.RINGER_MODE_SILENT
            MODE_VIBRO -> AudioManager.RINGER_MODE_VIBRATE
            else -> AudioManager.RINGER_MODE_NORMAL
        }
    }
}