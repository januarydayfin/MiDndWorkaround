package com.krayapp.dndworkaround.mvi

sealed interface Effects {
    data object ShowBackgroundInfo: Effects
    data object ShowPermissionDialog: Effects
}