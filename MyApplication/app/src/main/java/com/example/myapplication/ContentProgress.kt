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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun InfiniteProgress() {
    val infTransition = rememberInfiniteTransition(label = "transition")
    val translateAnimation by infTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "animation"
    )
//    val textMeasurer = rememberTextMeasurer()
//    val remainTime by remember { mutableStateOf(3) }

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

//        val textSize = density.run {
//            textMeasurer.measure(remainTime.toString()).size
//        }
//        drawText(
//            textMeasurer,
//            text = remainTime.toString(),
//            topLeft = Offset(center.x - textSize.width / 2, center.y - textSize.height / 2)
//        )
    }
}

@Composable
fun FiniteProgress(degree: Float, modifier: Modifier = Modifier) {
    Canvas (modifier = modifier) {
        val strokeSize = 6.dp.toPx()

        rotate(-90f) {
            drawArc(
                color = Color.Blue,
                size = Size(size.width - strokeSize, size.width - strokeSize),
                startAngle = -strokeSize / 2,
                sweepAngle = degree,
                useCenter = false,
                topLeft = Offset(strokeSize / 2, strokeSize / 2),
                style = Stroke(width = strokeSize, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun FiniteGradientProgress(degree: Float, modifier: Modifier = Modifier) {
    Canvas (modifier = modifier) {
        val strokeSize = 6.dp.toPx()

        rotate(-90f) {
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Blue,
                        Color.Transparent
                    ),
                    center = Offset(size.width / 2f, size.height / 2f)
                ),
                size = Size(size.width - strokeSize, size.width - strokeSize),
                startAngle = -strokeSize / 2,
                sweepAngle = degree,
                useCenter = false,
                topLeft = Offset(strokeSize / 2, strokeSize / 2),
                style = Stroke(width = strokeSize, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun ContentProgress(modifier: Modifier = Modifier, initialValue: Int = 3, completeListener: () -> Unit) {
    val remainProgress = remember { MutableTransitionState(-360f) }
    remainProgress.targetState = 0f
    val progressTransition = rememberTransition(remainProgress)
    val remainProgressAnimateValue by progressTransition.animateFloat(
        transitionSpec = {
            tween(
                durationMillis = initialValue * 1000,
                easing = LinearEasing
            )
        }, label = ""
    ) {
        it
    }

    val remainTime = remember {MutableTransitionState(initialValue + 1)}
    remainTime.targetState = 1
    val transition = rememberTransition(remainTime)
    val remainTimeAnimateValue by transition.animateInt(
        transitionSpec = {
            tween(
                durationMillis = initialValue * 1000,
                easing = LinearEasing
            )
        }, label = ""
    ) {
        if(remainProgressAnimateValue == remainProgress.targetState) {
            completeListener()
            0
        } else {
            it
        }
    }

    Box(contentAlignment = Alignment.Center) {
        FiniteProgress(remainProgressAnimateValue, modifier = modifier)
        Text(remainTimeAnimateValue.toString())
    }

}

@Preview
@Composable
fun ContentProgressPreview() {
    ContentProgress(modifier = Modifier.size(60.dp), 5, {})
}