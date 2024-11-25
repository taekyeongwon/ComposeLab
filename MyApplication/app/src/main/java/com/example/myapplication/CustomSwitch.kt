package com.example.myapplication

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun CustomSwitch(
    check: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    val minBound = with(density) { 0.dp.toPx() }
    val maxBound = with(density) { 150.dp.toPx() }
    val state by animateFloatAsState(
        targetValue = if (check) maxBound else minBound,
        animationSpec = tween(durationMillis = 1000),
        label = "custom_switch"
    )
    Box(
        modifier = modifier
            .size(width = 200.dp, height = 50.dp)
            .background(Color.LightGray)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(state.roundToInt(), 0) }
                .size(50.dp)
                .background(Color.Blue)
        )
    }
}