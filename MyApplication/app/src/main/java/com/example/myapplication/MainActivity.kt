package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        val context = LocalContext.current
                        val density = LocalDensity.current

                        val anchors = DraggableAnchors {
                            DragValue.Start at 0f
                            DragValue.End at 0f
                        }
                        val state = remember {
                            AnchoredDraggableState(
                                initialValue = DragValue.Start,
                                positionalThreshold = { distance: Float -> distance * 0.5f },
                                velocityThreshold = { with(density) { 100.dp.toPx() } },
                                snapAnimationSpec = tween(),
                                decayAnimationSpec = splineBasedDecay(density),
                            ).apply {
                                Log.d("test", "updateAnchors")
                                updateAnchors(anchors)
                            }
                        }
                        var expanded by remember { mutableStateOf(false) }
                        var minHeight by remember { mutableStateOf(0f) }
                        var maxHeight by remember { mutableStateOf(0f) }

                        var changingText by remember { mutableStateOf(
                            "asdfasdfasdf\nasdfasdfasdf\nasdfasdfasdf\nasdfasdfasdfasdfasdf\n" +
                                    "asdfasdfasdf\n" +
                                    "asdfasdfasdf\n" +
                                    "asdfasdfasdfasdfasdf\n" +
                                    "asdfasdfasdf\n" +
                                    "asdfasdfasdf\n" +
                                    "asdfasdf"
                        ) }

                        LaunchedEffect(minHeight, maxHeight, changingText) {
                            state.updateAnchors(DraggableAnchors {
                                DragValue.Start at minHeight
                                DragValue.End at maxHeight
                            })
                        }

                        var openDialog by remember { mutableStateOf(false) }
                        if(openDialog) {
                            CustomDialog(
                                onDismissRequest = { openDialog = false },
                                state = state,
                                changingText = changingText,
                                setChangingText = { changingText += it },
                                expanded = expanded,
                                setExpanded = { expanded = it },
                                minHeight = minHeight,
                                maxHeight = maxHeight,
                                setMinHeight = { minHeight = it },
                                setMaxHeight = { maxHeight = it }
                            )
                        }
                        Button(onClick = {openDialog = true}) {

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    state: AnchoredDraggableState<DragValue>,
    changingText: String = "",
    setChangingText: (String) -> Unit,
    expanded: Boolean = false,
    setExpanded: (Boolean) -> Unit,
    minHeight: Float = 0f,
    maxHeight: Float = 0f,
    setMinHeight: (Float) -> Unit,
    setMaxHeight: (Float) -> Unit
) {
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = {onDismissRequest()}) {
        DialogContent(
            state = state,
            changingText = changingText,
            minHeight = minHeight,
            setMinHeight = setMinHeight,
            maxHeight = maxHeight,
            setMaxHeight = setMaxHeight,
            header = {
                Box(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "This is a minimal dialog",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(onClick = {
                        setChangingText("qwerwqer\nqwerqwerqwer\nqwerqwerwerqwer\nqwerqwerqwer")
                    }, modifier = Modifier.align(Alignment.CenterEnd)) { }
                }
            },
            content = {
                Spacer(modifier = Modifier
                    .height(8.dp)
                    .padding(start = 16.dp, end = 16.dp))
                Text(
                    text = changingText,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            },
            bottom = {
                HorizontalDivider(
                    modifier = Modifier
                        .width(180.dp)
//                                .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                        .anchoredDraggable(
                            state = state, orientation = Orientation.Vertical
                        )
                        .clickable {
                            scope.launch {
                                setExpanded(!expanded)
                                if (expanded) {
                                    state.animateTo(DragValue.End)
                                } else {
                                    state.animateTo(DragValue.Start)
                                }
                            }

                        },
                    color = Color.Black
                )
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogContent(
    state: AnchoredDraggableState<DragValue>,
    changingText: String = "",
    minHeight: Float = 0f,
    maxHeight: Float = 0f,
    setMinHeight: (Float) -> Unit = {},
    setMaxHeight: (Float) -> Unit = {},
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    bottom: @Composable () -> Unit
) {
    val density = LocalDensity.current


    Surface(
        modifier = Modifier
            .fillMaxWidth()
//                .heightIn(min = 80.dp, max = 300.dp)
            .height(
                with(density) {
                    IntOffset(
                        0,
                        state
                            .requireOffset()
                            .roundToInt()
                    ).y
                        .toDp()
//                        .coerceIn(80.dp, 300.dp)
                }
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SubcomposeLayout { constraints ->
                    val headerPlaceable = subcompose("header") {
                        header()
                    }.first().measure(constraints.copy(maxHeight = Constraints.Infinity))

                    val placeable = subcompose("content") {
                        content()
                    }.map {
                        it.measure(constraints.copy(maxHeight = Constraints.Infinity))
                    }

                    val bottomPlaceable = subcompose("bottom") {
                        bottom()
                    }.first().measure(constraints)

                    // 컴포저블의 높이를 기록
                    setMinHeight(headerPlaceable.height.toFloat())
                    setMaxHeight(placeable.sumOf { it.height } + minHeight)
//                    Log.d("tests", "${minHeight.value} ${maxHeight.value}")

                    layout(constraints.maxWidth, constraints.maxHeight) {
                        headerPlaceable.place(0, 0)

                        if (state.targetValue == DragValue.End) {
                            var yPos = minHeight
                            placeable.forEach {
                                it.place(0, yPos.toInt())
                                yPos += it.height.toFloat()
                            }
                        }

                        bottomPlaceable.place(
                            constraints.maxWidth / 2 - bottomPlaceable.width / 2,
                            constraints.maxHeight - bottomPlaceable.height
                        )
                    }
                }
            }
        }
    }
}

enum class DragValue { Start, End }

@Preview(widthDp = 460)
@Composable
fun HeaderPreview() {
    Box(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "This is a minimal dialog",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(onClick = {
//            changingText += "qwerwqer\nqwerqwerqwer\nqwerqwerwerqwer\nqwerqwerqwer"
        }, modifier = Modifier.align(Alignment.CenterEnd)) { }
    }
}