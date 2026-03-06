package com.krayapp.dndworkaround.mvi

import com.krayapp.dndworkaround.R

data class UIState(
    val backgroundWorkType: BackgroundWorkType = BackgroundWorkType.RECEIVER,
    val dndMode: DndMode = DndMode.OFF
)

enum class BackgroundWorkType(val stringRes: Int) {
    SERVICE(stringRes = R.string.service),
    RECEIVER(stringRes = R.string.receiver)
}

enum class DndMode(val stringRes: Int, val iconRes: Int) {
    VIBRO(stringRes = R.string.vibro, iconRes = R.drawable.vibrate),
    OFF(
        stringRes = R.string.off,
        iconRes = R.drawable.off
    ),
    SILENT(stringRes = R.string.silent, iconRes = R.drawable.silent)
}
