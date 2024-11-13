package com.example.basicstatecodelab

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

//data class WellnessTask(val id: Int, var label: MutableState<String>, var checked: MutableState<Boolean> = mutableStateOf(false))
@Stable
class WellnessTask(
    val id: Int,
    var label: MutableState<String>,
    var checked: MutableState<Boolean>
//    initialChecked: Boolean = false
) {
//    var checked by mutableStateOf(initialChecked)

    override fun toString(): String {
        return "$id, ${label.toString()}, ${checked.toString()}"
    }
}

data class WellnessTaskLists(val list: List<WellnessTask>)