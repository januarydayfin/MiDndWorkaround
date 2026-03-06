package com.krayapp.dndworkaround.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.krayapp.dndworkaround.R
import com.krayapp.dndworkaround.theme.mSize
import com.krayapp.dndworkaround.theme.sSize
import com.krayapp.dndworkaround.theme.xsSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundExplanationBs(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        onDismissRequest = onDismiss, sheetState = sheetState,
        contentWindowInsets = {
            WindowInsets(0, 0, 0, 0)
        },
    ) {
        Column(modifier = Modifier.padding(horizontal = mSize)) {
            ExplanationItem(
                title = "${stringResource(R.string.receiver)} - Default",
                description = stringResource(R.string.receiver_explanation)
            )
            Space(sSize * 2)
            ExplanationItem(
                title = "${stringResource(R.string.service)} - Optional",
                description = stringResource(R.string.service_explanation)
            )
            Space(sSize)
        }
    }
}


@Composable
private fun ExplanationItem(title: String, description: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Space(xsSize)
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun BsPreview() {
    MaterialTheme {
        BackgroundExplanationBs { }
    }
}