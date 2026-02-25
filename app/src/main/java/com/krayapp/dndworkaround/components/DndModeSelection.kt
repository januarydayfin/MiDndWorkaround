package com.krayapp.dndworkaround.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.krayapp.dndworkaround.R
import com.krayapp.dndworkaround.theme.sSize

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DndModeSelection(onModeSelected: (DndMode) -> Unit = {}, initialState: DndMode) {
    var pickedState by remember { mutableStateOf(initialState) }

    LaunchedEffect(initialState) {
        pickedState = initialState
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.dnd_mode))

        ButtonGroup {
            DndMode.entries.forEach { mode ->
                ToggleButton(checked = pickedState == mode, onCheckedChange = {
                    pickedState = mode
                    onModeSelected(mode)
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(mode.iconRes),
                        contentDescription = null
                    )
                    Space(sSize)
                    Text(text = stringResource(mode.stringRes))
                }
            }
        }
    }
}

enum class DndMode(val stringRes: Int, val iconRes: Int) {
    VIBRO(stringRes = R.string.vibro, iconRes = R.drawable.vibrate),
    OFF(
        stringRes = R.string.off,
        iconRes = R.drawable.off
    ),
    SILENT(stringRes = R.string.silent, iconRes = R.drawable.silent)
}
