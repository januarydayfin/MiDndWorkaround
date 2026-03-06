package com.krayapp.dndworkaround.mvi

sealed interface MviIntent {
    data object ShowBackgroundInfo : MviIntent
    data object ShowPermissionDialog: MviIntent
    data class SelectDndMode(val mode: DndMode): MviIntent
    data class SelectBackgroundMode(val type: BackgroundWorkType): MviIntent
}
