package com.krayapp.dndworkaround.mvi

sealed interface MviEffects {
    data object ShowBackgroundInfo: MviEffects
    data object ShowPermissionDialog: MviEffects
}