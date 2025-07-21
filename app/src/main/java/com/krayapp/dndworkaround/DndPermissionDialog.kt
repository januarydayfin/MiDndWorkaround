package com.krayapp.dndworkaround

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.isVisible
import com.krayapp.dndworkaround.databinding.PermissionDialogBinding

class DndPermissionDialog(context: Context, private val dndGranted: () -> Boolean) :
    Dialog(context) {

    private var vb: PermissionDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = PermissionDialogBinding.inflate(LayoutInflater.from(context))

        setContentView(vb!!.root)

        window?.setBackgroundDrawable(context.getDrawable(R.drawable.background_surface_rounded))

        with(vb!!) {
            letAutostart.setOnClickListener { openAutostart() }
            letDnd.setOnClickListener { openDndSettings() }
            close.setOnClickListener { dismiss() }
        }
        updatePermissionViews()
    }

    fun updatePermissionViews() {
        vb?.letDnd?.isEnabled = !dndGranted()
        vb?.dndGranted?.isVisible = dndGranted()
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener { vb = null }
    }

    private fun openDndSettings() {
        context.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
    }

    private fun openAutostart() {
        try {
            context.startActivity(
                Intent().setComponent(
                    ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
                )
            )
        } catch (e: Exception) {
            Toast.makeText(context, R.string.no_xiaomi, Toast.LENGTH_SHORT).show()
        }
    }
}