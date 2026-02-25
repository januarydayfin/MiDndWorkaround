package com.krayapp.dndworkaround.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.krayapp.dndworkaround.R
import com.krayapp.dndworkaround.components.DndMode
import com.krayapp.dndworkaround.components.DndModeSelection
import com.krayapp.dndworkaround.recordMode
import com.krayapp.dndworkaround.setRecordMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Preview
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val initialState = context.recordMode().collectAsState(DndMode.OFF.toString())
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource(R.drawable.main_logo),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )


            DndModeSelection(
                initialState = DndMode.valueOf(initialState.value),
                onModeSelected = { mode ->
                    scope.launch {
                        context.setRecordMode(mode.toString())
                    }
                })

        }
    }
}