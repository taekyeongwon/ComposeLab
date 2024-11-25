package com.example.myapplication

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListScreen() {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val anchoredState = rememberAnchoredState(
        initialValue = DragValue.Start,
        positionalThreshold = { distance: Float -> distance * 0.5f },
        velocityThreshold = { with(density) { 100.dp.toPx() } },
        snapAnimationSpec = tween(),
        decayAnimationSpec = rememberSplineBasedDecay()
    ).apply {
        updateAnchors(DraggableAnchors {
            DragValue.Start at with(density) { 300.dp.toPx() }
            DragValue.End at with(density) { screenHeight.toPx() }
        })
    }

    Column {
        DraggableList(anchoredState)
    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun DraggableList(state: AnchoredDraggableState<DragValue>) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var scrollEnabled by remember { mutableStateOf(true) }
    var isScrollDown by remember { mutableStateOf(false) }
    Button(onClick = {
        expanded = !expanded
    }) { }
    val scrollState = rememberLazyListState()

    val nestedScrollConnection = object : NestedScrollConnection {
//        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//
//            scope.launch {
////                scrollState.dispatchRawDelta(0f)
////                scrollState.dispatchRawDelta(available.y)
//                state.dispatchRawDelta(available.y)
//            }
//
//            println("Received onPreScroll callback. ${available}")
//            return Offset.Zero
//        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            println("Received onPostScroll callback. $consumed $available")
            scope.launch {
//                scrollState.dispatchRawDelta(0f)

                state.dispatchRawDelta(consumed.y)
            }
            return Offset.Zero
        }
    }

//    SideEffect {
//        Log.d("test", scrollState.canScrollForward.toString())
//        Log.d("test", scrollState.canScrollBackward.toString())
////        scrollState.stopScroll()
//        scrollState.interactionSource.interactions.collect {
//            Log.d("test", it.toString())
//        }
//    }

    if(expanded) {
        Box(modifier = Modifier) {
            LazyColumn(
                state = scrollState,
                userScrollEnabled = scrollEnabled,
                modifier = Modifier
                    .width(300.dp)
                    .height(
                        with(density) {
                            state
                                .requireOffset()
                                .roundToInt()
                                .toDp()
                        }
                    )
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()

                                //pointer pass 종류에 따라 뭐가 다른지. 일단 이 changes에서 현재 마우스 포인터의 포지션을 알아올 수 있으므로
                                //해당 포지션이 scrollState.layoutInfo의 가장 하단에 닿았을 때 변경되는 값 만큼 anchor state를
                                //변경해주면 될 것 같음!
                                when(event.type) {
                                    PointerEventType.Move -> {
                                        if(event.changes.isNotEmpty()) {
                                            val currentPositionY =
                                                event.changes[event.changes.size - 1].position.y
                                            val scrollEndOffset =
                                                scrollState.layoutInfo.viewportEndOffset -
                                                        with(density) { 10.dp.toPx() }
//                                            val scrollEndOffsetPx =
//                                                with(density) { 40.dp.toPx() }  //선택 영역 범위 제한

//                                            if (abs(currentPositionY - scrollEndOffset) < scrollEndOffsetPx) {
//                                                scrollEnabled = false
//                                                state.dispatchRawDelta(currentPositionY - scrollEndOffset)
//                                            }

                                            val lastPositionY = event.changes[event.changes.size - 1].previousPosition.y

                                            //아래로 빠르게 드래그하는 경우 prev 600 current 800 endoffset 788
                                            //위로 빠르게 드래그하는 경우 prev 800 current 600 endoffset 788
                                            if(lastPositionY < currentPositionY && currentPositionY > scrollEndOffset) {
                                                scrollEnabled = false
                                                isScrollDown = true
                                                state.dispatchRawDelta(currentPositionY - scrollEndOffset)
                                            } else if(lastPositionY >= currentPositionY && currentPositionY < scrollEndOffset && isScrollDown) {
                                                scrollEnabled = false
                                                state.dispatchRawDelta(currentPositionY - scrollEndOffset)
                                            }
                                        }
                                    }
                                    PointerEventType.Release -> {
                                        scrollEnabled = true
                                        isScrollDown = false
                                        scope.launch {
                                            state.settle(state.lastVelocity)
                                        }
                                    }
                                }
                            }
                        }
                    }

            ) {
                items(20) {
                    Text(text = "Item $it", modifier = Modifier.padding(16.dp))
                }

                if(state.requireOffset() == state.anchors.maxAnchor()) {
                    item {
                        Column(modifier = Modifier.height(50.dp)) { }
                    }
                }
            }


            if(state.requireOffset() < state.anchors.maxAnchor()) {
                HorizontalDivider(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(180.dp)
                        .anchoredDraggable(state = state, orientation = Orientation.Vertical)
                        .padding(10.dp)

                )
                //리스트 드래그 중에 여기 닿았을 때 늘어나도록 하려면 pointerInput써서 할 수 있지 않을까?
            // 아니면 anchoredDraggable을 pointerInput으로 직접 구현도 가능하지 않을까?
            } else {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            state.animateTo(DragValue.Start)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) { }
            }
        }

    }
}
