package com.krayapp.dndworkaround

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krayapp.dndworkaround.data.DndRepository
import com.krayapp.dndworkaround.mvi.MviEffects
import com.krayapp.dndworkaround.mvi.MviIntent
import com.krayapp.dndworkaround.mvi.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DndRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: MutableStateFlow<UIState> = _uiState

    private val _effects = MutableSharedFlow<MviEffects>()
    val effects: MutableSharedFlow<MviEffects> = _effects

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
    fun onIntent(mviIntent: MviIntent) {
        launchInIo {
            when (mviIntent) {
                is MviIntent.ShowBackgroundInfo -> {
                    _effects.emit(MviEffects.ShowBackgroundInfo)
                }

                is MviIntent.SelectBackgroundMode -> {
                    repository.saveBackgroundMode(mviIntent.type)
                    _uiState.emit(_uiState.value.copy(backgroundWorkType = mviIntent.type))
                }
                is MviIntent.SelectDndMode -> {
                    repository.saveDndMode(mviIntent.mode)
                    _uiState.emit(_uiState.value.copy(dndMode = mviIntent.mode))
                }

                MviIntent.ShowPermissionDialog -> {
                    _effects.emit(MviEffects.ShowPermissionDialog)
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
