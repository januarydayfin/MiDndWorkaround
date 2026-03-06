package com.krayapp.dndworkaround.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.krayapp.dndworkaround.mvi.BackgroundWorkType
import com.krayapp.dndworkaround.R
import com.krayapp.dndworkaround.theme.sSize

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BackgroundWorkSelection(
    initialState: BackgroundWorkType,
    onModeSelected: (BackgroundWorkType) -> Unit = {},
    openBackgroundInfo: () -> Unit = {}
) {
    var pickedState by remember { mutableStateOf(initialState) }

    LaunchedEffect(initialState) {
        pickedState = initialState
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = stringResource(R.string.background_type),
                color = MaterialTheme.colorScheme.onSurface
            )
            Space(sSize)
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                    openBackgroundInfo()
                },
                imageVector = ImageVector.vectorResource(R.drawable.info),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }


        Space(sSize)
        ButtonGroup {
            BackgroundWorkType.entries.forEach { mode ->
                ToggleButton(checked = pickedState == mode, onCheckedChange = {
                    pickedState = mode
                    onModeSelected(mode)
                }) {
                    Text(text = stringResource(mode.stringRes))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SelectorPreview() {
    MaterialTheme {
        BackgroundWorkSelection(initialState = BackgroundWorkType.SERVICE)
    }
}


