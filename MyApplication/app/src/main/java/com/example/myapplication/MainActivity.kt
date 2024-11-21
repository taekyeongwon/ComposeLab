package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        val context = LocalContext.current
                        val density = LocalDensity.current
                        val changingText = "asdfasdfasdf\nasdfasdfasdf\nasdfasdfasdf\nasdfasdfasdfasdfasdf\n" +
                                    "asdfasdfasdf\n" +
                                    "asdfasdfasdf\n" +
                                    "asdfasdfasdfasdfasdf\n" +
                                    "asdfasdfasdf\n" +
                                    "asdfasdfasdf\n" +
                                    "asdfasdf"


                        val dragState = rememberDraggableState(
                            state = CustomDraggableHolder(
                                text = changingText
                            )
                        )
                        val anchoredState = rememberAnchoredState(
                            initialValue = DragValue.Start,
                            positionalThreshold = { distance: Float -> distance * 0.5f },
                            velocityThreshold = { with(density) { 100.dp.toPx() } },
                            snapAnimationSpec = tween(),
                            decayAnimationSpec = rememberSplineBasedDecay()
                        )

                        LaunchedEffect(dragState.minHeight, dragState.maxHeight, dragState.text) {
                            anchoredState.updateAnchors(DraggableAnchors {
                                DragValue.Start at dragState.minHeight
                                DragValue.End at dragState.maxHeight
                            })
                        }

                        var openDialog by remember { mutableStateOf(false) }
                        if(openDialog) {
                            CustomDialog(
                                onDismissRequest = { openDialog = false },
                                state = anchoredState,
                                changingText = dragState.text,
                                setChangingText = { dragState.textAppend(it) },
                                expanded = dragState.expanded,
                                setExpanded = { dragState.setExpand(it) },
                                minHeight = dragState.minHeight,
                                setHeight = { min, max -> dragState.setHeight(min, max) }
                            )
                        }
                        Button(onClick = {openDialog = true}) {

                        }
                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .background(Color.Blue)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    Toast
                                        .makeText(baseContext, "Kotlin World", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        )
                        CompositionLocalProvider(
                            LocalRippleConfiguration provides RippleConfiguration(
                                rippleAlpha = RippleAlpha(
                                    0f,
                                    0f,
                                    0f,
                                    0f
                                )
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(300.dp)
                                    .background(Color.Blue)
                                    .clickable(
//                                        interactionSource = remember{ MutableInteractionSource() },
//                                        indication = null
                                    ) {
                                        Toast
                                            .makeText(
                                                baseContext,
                                                "Kotlin World",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                            )
//                            Button(onClick = {}) {
//
//                            }
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
    setHeight: (Float, Float) -> Unit
) {
    Dialog(onDismissRequest = {onDismissRequest()}) {
        DialogContent(
            state = state,
            minHeight = minHeight,
            setHeight = setHeight,
            header = {
                DialogHeader(setChangingText, onDismissRequest)
            },
            content = {
                DialogBody(changingText)
            },
            bottom = {
                DialogBottom(state, expanded, setExpanded)
            }
        )
    }
}

@Composable
fun DialogHeader(
    setChangingText: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    Row(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "This is a minimal dialog asdf",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
                //텍스트가 가려질 때 처럼 콘텐츠 가려질 때 처리
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
            Button(onClick = {
                setChangingText("qwerwqer\nqwerqwerqwer\nqwerqwerwerqwer\nqwerqwerqwer")
            }) { }
            ContentProgress(Modifier.size(60.dp), 5) {
                onDismissRequest()
            } //프로그레스 끝날 때 리스너 받기
        }

    }
}

@Composable
fun DialogBody(changingText: String = "") {
    Spacer(modifier = Modifier
        .height(8.dp)
        .padding(start = 16.dp, end = 16.dp))
    Text(
        text = changingText,
        textAlign = TextAlign.Justify,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogBottom(
    state: AnchoredDraggableState<DragValue>,
    expanded: Boolean = false,
    setExpanded: (Boolean) -> Unit = {},
) {
    val scope = rememberCoroutineScope()

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogContent(
    state: AnchoredDraggableState<DragValue>,
    minHeight: Float = 0f,
    setHeight: (Float, Float) -> Unit = {_ ,_ -> },
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
                    setHeight(
                        headerPlaceable.height.toFloat(),
                        placeable.sumOf { it.height } + minHeight
                    )
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

@Preview(widthDp = 260)
@Composable
fun HeaderPreview() {
    DialogHeader(
        setChangingText = {},
        onDismissRequest = {}
    )
}