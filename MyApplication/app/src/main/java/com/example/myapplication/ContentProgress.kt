package com.example.myapplication

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ContentProgress() {

    val infTransition = rememberInfiniteTransition(label = "transition")
    val translateAnimation by infTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "animation"
    )
    val textMeasurer = rememberTextMeasurer()
    var remainTime by remember { mutableStateOf(3) }

    Canvas (modifier = Modifier.size(60.dp)) {
        val startAngle = 5f
        val sweepAngle = 350f
        val strokeSize = 6.dp.toPx()

        rotate(translateAnimation) {
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Blue,
                        Color.Blue.copy(0f)
                    ),
                    center = Offset(size.width / 2f, size.height / 2f)
                ),
                size = Size(size.width - strokeSize, size.width - strokeSize),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(strokeSize / 2, strokeSize / 2),
                style = Stroke(width = strokeSize, cap = StrokeCap.Round)
            )
        }

        val textSize = density.run {
            textMeasurer.measure(remainTime.toString()).size
        }
        drawText(
            textMeasurer,
            text = remainTime.toString(),
            topLeft = Offset(center.x - textSize.width / 2, center.y - textSize.height / 2)
        )
    }
}

@Preview
@Composable
fun TestProgress() {
    val currentState = remember {MutableTransitionState(4)}
    currentState.targetState = 1
    val transition = rememberTransition(currentState)
    val translateValue by transition.animateInt(
        transitionSpec = { tween(durationMillis = 3000, easing = LinearEasing) }, label = ""
    ) { it }

    Text(translateValue.toString())
}