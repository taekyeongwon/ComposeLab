package com.example.basicstatecodelab

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList


@Composable
fun WellnessTaskList(
    list: ImmutableList<WellnessTask>,
    onCheckedTask: (WellnessTask, Boolean) -> Unit,
    onCloseTask: () -> Unit,
    modifier: Modifier = Modifier
) {
//    Log.d("test", "lists : ${list.hashCode()}")
    Log.d("test", onCheckedTask.hashCode().toString())
    Log.d("test", onCloseTask.hashCode().toString())
//    TestButton(onCloseTask)
    var t by remember { mutableStateOf(0) }
    LazyColumn(
        modifier = modifier.height(500.dp)
    ) {
        Log.d("test", "lazycolumn")

        items(
            items = list,
            key = { task -> task.id }
        ) { task ->
            Log.d("test", "items ${task.hashCode()}")

            WellnessTaskItem(
                taskName = task.label.value,
                checked = {task.checked.value},
                onCheckedChange = { checked -> onCheckedTask(task, checked)},
                onClose = { onCloseTask() }
            )

//            TestTask(task)
//            Button(onClick = { t++ }) { }
//            Text(t.toString())
        }
    }
}

@Composable
fun TestTask(task: WellnessTask) {
    Text(task.label.value)
}

@Composable
fun TestButton(onClose: () -> Unit) {
    Button(onClick = { onClose() }) {
        Text(text = "tests")
    }
}

//private fun getWellnessTask() = List(30) { i ->
//    WellnessTask(i, mutableStateOf("Task # $i"))
//}