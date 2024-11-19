@file:OptIn(ExperimentalFoundationApi::class)

package com.example.myapplication

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

enum class DragValue { Start, End }

class CustomDraggableHolder(
    minHeight: Float = 0f,
    maxHeight: Float = 0f,
    expanded: Boolean = false,
    text: String = ""
) {
    var minHeight by mutableStateOf(minHeight)
        private set
    var maxHeight by mutableStateOf(maxHeight)
        private set
    var expanded by mutableStateOf(expanded)
        private set
    var text by mutableStateOf(text)
        private set

    fun setHeight(minHeight: Float, maxHeight: Float) {
        this.minHeight = minHeight
        this.maxHeight = maxHeight
    }

    fun setExpand(expanded: Boolean) {
        this.expanded = expanded
    }

    fun textAppend(text: String) {
        this.text += text
    }

    companion object {
//        val Saver: Saver<CustomDraggableHolder, CustomDraggableHolder> = Saver( //왜 이걸로 저장은 안되지? 직렬화 해서 저장하는게 아니기 때문? -> 단순한 밸류 하나 저장할 때 사용
//            save = { it },
//            restore = { CustomDraggableHolder(it.minHeight, it.maxHeight, it.expanded, it.text) }
//        )
        val Saver = listSaver(
            save = { listOf(it.minHeight, it.maxHeight, it.expanded, it.text) },
            restore = { CustomDraggableHolder(
                it[0] as Float,
                it[1] as Float,
                it[2] as Boolean,
                it[3] as String
            ) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberAnchoredState(
    initialValue: DragValue,
    positionalThreshold: (distance: Float) -> Float,
    velocityThreshold: () -> Float,
    snapAnimationSpec: AnimationSpec<Float>,
    decayAnimationSpec: DecayAnimationSpec<Float>
): AnchoredDraggableState<DragValue> = rememberSaveable(
    saver = AnchoredDraggableState.Saver(
        snapAnimationSpec,
        decayAnimationSpec,
        positionalThreshold,
        velocityThreshold
    )
) {
    AnchoredDraggableState(
        initialValue = initialValue,
        positionalThreshold,
        velocityThreshold,
        snapAnimationSpec,
        decayAnimationSpec
    )
}

@Composable
fun rememberDraggableState(
    state: CustomDraggableHolder
): CustomDraggableHolder = rememberSaveable(saver = CustomDraggableHolder.Saver) {
    CustomDraggableHolder(
        state.minHeight,
        state.maxHeight,
        state.expanded,
        state.text
    )
}
