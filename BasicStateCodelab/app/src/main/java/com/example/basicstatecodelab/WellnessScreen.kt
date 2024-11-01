package com.example.basicstatecodelab

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.collections.immutable.toImmutableList

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier,
    wellnessViewModel: WellnessViewModel = viewModel()
) {
    Column(modifier = modifier) {
        var test by remember { mutableStateOf(0) }
        StatefulCounter(modifier)

//        val list = remember { getWellnessTask().toMutableStateList() }
//        val list = remember { mutableStateListOf<WellnessTask>() }
//        list.addAll(getWellnessTask())
//        val wellnessList = remember { mutableStateOf(WellnessTaskLists(list)) }

        val onChecked = remember {
            { task: WellnessTask, checked: Boolean ->
                wellnessViewModel.changeTaskChecked(task, checked)
            }
        }
        val onClose = remember {
            { task: WellnessTask ->
                wellnessViewModel.remove(task)
            }
        }

        WellTest(wellnessViewModel.tasks[0], onClick = { task -> test++ })
        WellnessTaskList(   //왜 리컴포지션 스킵이 안되지? 일단 
            list = wellnessViewModel.taskList.toImmutableList(),
            onCheckedTask = wellnessViewModel::changeTaskChecked, //{ task, checked -> onChecked(task, checked) },
//            onCloseTask = wellnessViewModel::remove //메서드 참조값 넘기는 경우 lists 매개변수도 Immutable하다면 내부 LazyColumn 스킵.
            onCloseTask = wellnessViewModel::remove //{ task -> onClose(task) }
            // tasks는 state로 관찰하는 값이므로 WellnessTaskList는 리컴포지션
        )   //이렇게 람다에서 viewmodel을 캡처해도 리컴포지션 범위가 WellnessScreen까지 포함되지 않으므로 WellnessTaskList는 리컴포지션 되지 않는다.
        Text(test.toString()) //test를 관찰하는 컴포저블이 있는 경우
        //WellnessTaskList의 list가 unstable이므로 전체 리컴포지션이 발생한다.
    }


}

@Composable
fun WellTest(
    task: WellnessTask,
    onClick: (WellnessTask) -> Unit
) {
    Text(text = task.label.value,
        modifier = Modifier.clickable { onClick(task) }
    )
}
