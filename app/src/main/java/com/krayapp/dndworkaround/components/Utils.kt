package com.krayapp.dndworkaround.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.Space(size: Dp = 0.dp) {
    Spacer(
        modifier = Modifier
            .width(size)
    )
}

@Composable
fun ColumnScope.Space(size: Dp = 0.dp) {
    Spacer(
        modifier = Modifier
            .height(size)
    )
}
