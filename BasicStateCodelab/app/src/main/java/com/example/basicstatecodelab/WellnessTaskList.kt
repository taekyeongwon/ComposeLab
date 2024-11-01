package com.example.basicstatecodelab

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList


@Composable
fun WellnessTaskList(
    list: ImmutableList<WellnessTask>,
    onCheckedTask: (WellnessTask, Boolean) -> Unit,
    onCloseTask: (WellnessTask) -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("test", "lists : ${list.hashCode()}")
    Log.d("test", onCheckedTask.hashCode().toString())
    Log.d("test", onCloseTask.hashCode().toString())
    LazyColumn(
        modifier = modifier.height(500.dp)
    ) {
        items(
            items = list,
            key = { task -> task.id }
        ) { task ->
            WellnessTaskItem(
                taskName = task.label.value,
                checked = task.checked,
                onCheckedChange = {checked -> onCheckedTask(task, checked)},
                onClose = { onCloseTask(task) }
            )
        }
    }
}