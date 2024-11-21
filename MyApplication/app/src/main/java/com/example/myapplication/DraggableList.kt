package com.example.myapplication

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
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
    Log.d("test", with(density) {
        IntOffset(
            0,
            anchoredState
                .requireOffset()
                .roundToInt()
        ).y.toDp().toString()
    })

    Column {
        DraggableList(anchoredState)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableList(state: AnchoredDraggableState<DragValue>) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    Button(onClick = {
        expanded = !expanded
    }) { }

    if(expanded) {
        Box {
            LazyColumn(
                modifier = Modifier
                    .width(300.dp)
                    .height(
                        with(density) {
                            state
                                .requireOffset()
                                .roundToInt().toDp()
                        }
                    )
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