package com.krayapp.dndworkaround

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krayapp.dndworkaround.data.DndRepository
import com.krayapp.dndworkaround.mvi.Effects
import com.krayapp.dndworkaround.mvi.Intent
import com.krayapp.dndworkaround.mvi.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DndRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: MutableStateFlow<UIState> = _uiState

    private val _effects = MutableSharedFlow<Effects>()
    val effects: MutableSharedFlow<Effects> = _effects

    init {
        launchInIo {
            val initialDndMode = repository.getDndMode()
            val initialBackgroundMode = repository.getBackgroundMode()
            _uiState.value = _uiState.value.copy(
                dndMode = initialDndMode,
                backgroundWorkType = initialBackgroundMode
            )
        }
    }
    fun onIntent(intent: Intent) {
        launchInIo {
            when (intent) {
                is Intent.ShowBackgroundInfo -> {
                    _effects.emit(Effects.ShowBackgroundInfo)
                }

                is Intent.SelectBackgroundMode -> {
                    repository.saveBackgroundMode(intent.type)
                    _uiState.emit(_uiState.value.copy(backgroundWorkType = intent.type))
                }
                is Intent.SelectDndMode -> {
                    repository.saveDndMode(intent.mode)
                    _uiState.emit(_uiState.value.copy(dndMode = intent.mode))
                }

                Intent.ShowPermissionDialog -> {
                    _effects.emit(Effects.ShowPermissionDialog)
                }
            }
        }
    }



    private inline fun launchInIo(crossinline block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
        }
    }
}
