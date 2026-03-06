package com.krayapp.dndworkaround.mvi

sealed interface Intent {
    data object ShowBackgroundInfo : Intent
    data object ShowPermissionDialog: Intent
    data class SelectDndMode(val mode: DndMode): Intent
    data class SelectBackgroundMode(val type: BackgroundWorkType): Intent
}
