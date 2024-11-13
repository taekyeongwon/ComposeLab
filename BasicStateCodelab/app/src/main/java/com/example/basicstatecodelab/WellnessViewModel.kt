package com.example.basicstatecodelab

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class WellnessViewModel: ViewModel() {
    private val _tasks = getWellnessTask().toMutableStateList()
    //추가, 삭제에 대해서만 관찰함.
    val tasks: List<WellnessTask>
        get() = _tasks

//    @Stable
    var taskList = mutableStateListOf<WellnessTask>().also {
        it.addAll(getWellnessTask())
    }
        private set

    fun remove2() {
        Log.d("test", "remove2")
    }

    fun remove(item: WellnessTask) {
//        _tasks.find { items -> items.id == item.id }?.label?.value = "checekd"
        _tasks.remove(item)
    }

    fun changeTaskChecked(item: WellnessTask, checked: Boolean) =
        _tasks.find { it.id == item.id }?.let { task ->
            task.checked.value = checked
        }
}

private fun getWellnessTask() = List(30) { i ->
    WellnessTask(i, mutableStateOf("Task # $i"), mutableStateOf(false))
}