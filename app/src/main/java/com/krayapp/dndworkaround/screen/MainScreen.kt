package com.krayapp.dndworkaround.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.krayapp.dndworkaround.MainViewModel
import com.krayapp.dndworkaround.R
import com.krayapp.dndworkaround.components.BackgroundExplanationBs
import com.krayapp.dndworkaround.components.BackgroundWorkSelection
import com.krayapp.dndworkaround.components.DndModeSelection
import com.krayapp.dndworkaround.components.PermissionDialog
import com.krayapp.dndworkaround.components.Space
import com.krayapp.dndworkaround.components.dndGranted
import com.krayapp.dndworkaround.components.tryToApplyMode
import com.krayapp.dndworkaround.mvi.BackgroundWorkType
import com.krayapp.dndworkaround.mvi.MviEffects
import com.krayapp.dndworkaround.mvi.MviIntent
import com.krayapp.dndworkaround.theme.mSize
import com.krayapp.dndworkaround.theme.xlSize
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(enableForegroundService: () -> Unit, disableForegroundService: () -> Unit) {
    val viewModel: MainViewModel = koinViewModel()
    val context = LocalContext.current

    val uiState = viewModel.uiState.collectAsState()
    var showExplanationBackgroundBs by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(!context.dndGranted()) }


    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest {
            when (it) {
                MviEffects.ShowBackgroundInfo -> showExplanationBackgroundBs = true
                MviEffects.ShowPermissionDialog -> showPermissionDialog = true
            }
        }
    }

    if (showExplanationBackgroundBs)
        BackgroundExplanationBs {
            showExplanationBackgroundBs = false
        }

    if (showPermissionDialog)
        PermissionDialog {
            showPermissionDialog = false
        }


    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.fillMaxWidth().aspectRatio(2f),
                painter = painterResource(R.drawable.main_logo),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Space(xlSize)

            OutlinedButton(onClick = {
                viewModel.onIntent(MviIntent.ShowPermissionDialog)
            }) {
                Text(stringResource(R.string.let_permission))
            }
            Space(xlSize * 3)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                DndModeSelection(
                    initialState = uiState.value.dndMode,
                    onModeSelected = { mode ->
                        viewModel.onIntent(MviIntent.SelectDndMode(mode))
                        context.tryToApplyMode(mode)
                    })

                Space(mSize * 2)
                BackgroundWorkSelection(
                    initialState = uiState.value.backgroundWorkType,
                    onModeSelected = { mode ->
                        viewModel.onIntent(MviIntent.SelectBackgroundMode(mode))
                        when (mode) {
                            BackgroundWorkType.SERVICE -> enableForegroundService()
                            BackgroundWorkType.RECEIVER -> disableForegroundService()
                        }
                    },
                    openBackgroundInfo = {
                        viewModel.onIntent(MviIntent.ShowBackgroundInfo)
                    })

            }
        }
    }
}
