package com.krayapp.dndworkaround.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.krayapp.dndworkaround.R
import com.krayapp.dndworkaround.theme.AppTheme
import com.krayapp.dndworkaround.theme.lSize
import com.krayapp.dndworkaround.theme.mSize
import com.krayapp.dndworkaround.theme.sSize
import com.krayapp.dndworkaround.theme.xlSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var dndGranted by remember { mutableStateOf(context.dndGranted()) }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                dndGranted = context.dndGranted()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(modifier = Modifier.clip(RoundedCornerShape(xlSize))) {
            val context = LocalContext.current
            Column(
                modifier = Modifier.padding(mSize),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    context.openAutostart()
                }) {
                    Text(text = stringResource(R.string.enable_autostart))
                }
                Space(sSize)
                Button(onClick = {
                    context.openDndSettings()
                }, enabled = !dndGranted) {
                    if (dndGranted) {
                        Icon(
                            painter = painterResource(R.drawable.check),
                            contentDescription = null
                        )
                        Space(sSize)
                    }
                    Text(text = stringResource(R.string.give_dnd))
                }
                Space(lSize)
                OutlinedButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.close))
                }
            }
        }
    }
}


@Composable
@Preview
private fun DialogPreview() {
    AppTheme {
        PermissionDialog { }
    }
}