package com.example.basicstatecodelab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.basicstatecodelab.ui.theme.BasicStateCodelabTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<WellnessViewModel>(
//        factoryProducer = {
//            SavedStateViewModelFactory(
//                application = application,
//                this,
//                intent?.extras
//            )
//        }

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        Log.d("test", "#0")
//        CoroutineScope(Dispatchers.Main).launch {
//            Log.d("test", "#1")
//        }
//        Thread.sleep(1000)
//        Log.d("test", "#2")
        setContent {
            BasicStateCodelabTheme {
                var tempValue by remember { mutableStateOf(1) }
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                ) {

//                    TestScreen2()
//                    TestScreen2()
//                    Column(modifier = Modifier
//                        .padding(padding),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        val m = Modifier
//                            .align(Alignment.End)
//                            .clickable { tempValue++ }
//                        val test = ReturnTest(tempValue, modifier = m)
////                            .myBackground(Color.Red))
//                        SideEffect {
//                            Log.d("testtest", test.toString())
//                        }
//                        MyScreen(tempValue, m)
////                        Text(tempValue.toString())
//                        Button({ tempValue++ }, modifier = Modifier.align(Alignment.End)) {
//                            Text("click")
//                        }
//                    }
//                    var test by remember { mutableStateOf(1) }
//                    val m = Modifier.myBg()
//                    Button(onClick = {test++}) {
//                        Text(test.toString(), modifier = m)
//                    }

                    val viewModel: WellnessViewModel = viewModel()
                    Log.d("test", "surface")
                    WellnessScreen(
                        wellnessViewModel = viewModel
//                        onCheckedTask = { task: WellnessTask, checked: Boolean ->
//                            viewModel.changeTaskChecked(task, checked)
//                        },
//                        onCloseTask = { task: WellnessTask ->
//                            viewModel.remove(task)
////                            Log.d("test", unstableTest.label.value)
//                        }
                    )
                }
//                Column(modifier = Modifier.padding(top = 16.dp)) {
//                    GreetingScreen("user1")
//                    GreetingScreen("user2")
//                }
            }
        }
    }
}

@Composable
fun TestScreen2() {
//    var test by remember {
//        mutableStateOf(1)
//    }
    val tt by remember {
        mutableStateOf(
            WellnessTask(
                0, mutableStateOf("asdf"), mutableStateOf(false)
            )
        )
    }
    Button(
        onClick = {tt.checked.value = !tt.checked.value}
    ) {

    }
    Text(tt.label.value)    //todo tt.checked.value면 TestScreen2부터 리컴포지션 발생하고, TestTask2는 스킵된다.
    //애초에 변경된 값 (tt.checked)를 안 읽는데 캡처된 값을 관찰하지 않으므로 리컴포지션 자체가 발생을 안함!!
    TestTask2(tt)

}

@Composable
fun TestTask2(task : WellnessTask) {
    Text(task.label.value)
}

@Composable
fun TestScreen() {
    var test by remember { mutableStateOf(0) }
    Column(modifier = Modifier
        .clickable { test++ }
        .size(100.dp)
        .background(Color.Red)) {
        Text(test.toString())
    }
}

@Composable
fun Modifier.myBackground(): Modifier {
    val color = LocalContentColor.current
    SideEffect {
        Log.d("test", "modifier")
    }

    return this then Modifier.background(color.copy(alpha = 0.5f))
}

fun Modifier.myBg(): Modifier {
    Log.d("test", "modifier")
    return this then Modifier.background(Color.Red)
}

fun Modifier.myBackground(color: Color): Modifier {
//    SideEffect {
    Log.d("test", "modifier2")
//    }
    return this then Modifier
        .padding(16.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(color)
}

// Modifier factory
fun Modifier.circle(color: Color) = this then CircleElement(color)

// ModifierNodeElement
private data class CircleElement(val color: Color) : ModifierNodeElement<CircleNode>() {
    override fun create() = CircleNode(color)

    override fun update(node: CircleNode) {
        node.color = color
    }
}

// Modifier.Node
private class CircleNode(var color: Color) : DrawModifierNode, Modifier.Node(),
    CompositionLocalConsumerModifierNode {
    private val alpha = Animatable(1f)
    override fun ContentDrawScope.draw() {

        val currentColor = currentValueOf(LocalContentColor)
        drawCircle(color)
    }
}

@Composable
fun Modifier.fade2(enable: Boolean): Modifier {
    val alpha by animateFloatAsState(if (enable) 0.5f else 1.0f)
    return this then Modifier.graphicsLayer { this.alpha = alpha }
}

fun Modifier.fade(enable: Boolean): Modifier = composed {
    val alpha by animateFloatAsState(if (enable) 0.5f else 1.0f)
    graphicsLayer { this.alpha = alpha }
}

@Composable
fun MyScreen(test: Int, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalContentColor provides Color.Green) {
        // Background modifier created with green background
        val backgroundModifier = Modifier.myBackground()

        // LocalContentColor updated to red
        CompositionLocalProvider(LocalContentColor provides Color.Red) {
            Text("${test.toString()} myscreen", modifier = modifier)
            // Box will have green background, not red as expected.
            Text("asdf", modifier = Modifier.myBackground(Color.Blue))
        }
    }
}

@Composable
private fun ReturnTest(test: Int, modifier: Modifier = Modifier): Int {
    var tempValue by remember { mutableStateOf(1) }
    Column {
        Text(test.toString(), modifier = modifier)
        Button({ tempValue++ }, modifier = Modifier.align(Alignment.End)) {
            Text("clickReturn")
        }
    }


    return 1
}

@Composable
private fun AlertCard() {
    var currentTargetElevation by remember { mutableStateOf(8.dp) }

    LaunchedEffect(Unit) {
        // Start the animation
        Log.d("test", "before")
        currentTargetElevation = 1.dp

        Log.d("test", "after")
    }
    SideEffect {
        Log.d("test", "side")
    }

    Log.d("test", "outer")
    val animatedElevation = animateDpAsState(
        targetValue = currentTargetElevation,
        animationSpec = tween(durationMillis = 500),
        finishedListener = {
            currentTargetElevation = 8.dp
        }
    )
    val test by rememberUpdatedState(animatedElevation)

    androidx.compose.material.Card(elevation = animatedElevation.value) {
        Column {
            AlertHeader {

            }
            RallyDivider(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
            )
            AlertItem("test")
        }
    }
    //얘는 왜 계속 무한으로 변경이 안됨
//    Card(
//        elevation = CardDefaults.elevatedCardElevation(defaultElevation = test.value),
//
//    ) {
//        Column {
//            AlertHeader {
//
//            }
//            RallyDivider(
//                modifier = Modifier.padding(start = 12.dp, end = 12.dp)
//            )
//            AlertItem("test")
//        }
//    }
//    Card(
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant,
//        ),
//        modifier = Modifier
//            .size(width = 240.dp, height = 100.dp)
//    ) {
//        Text(
//            text = "Filled",
//            modifier = Modifier
//                .padding(16.dp),
//            textAlign = TextAlign.Center,
//        )
//    }
}

@Composable
fun RallyDivider(modifier: Modifier = Modifier) {
    Divider(color = MaterialTheme.colorScheme.background, thickness = 1.dp, modifier = modifier)
}

@Composable
private fun AlertHeader(onClickSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Alerts",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        TextButton(
            onClick = onClickSeeAll,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "SEE ALL",
            )
        }
    }
}

@Composable
private fun AlertItem(message: String) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            // Regard the whole row as one semantics node. This way each row will receive focus as
            // a whole and the focus bounds will be around the whole row content. The semantics
            // properties of the descendants will be merged. If we'd use clearAndSetSemantics instead,
            // we'd have to define the semantics properties explicitly.
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = message
        )
        IconButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.Top)
                .clearAndSetSemantics {}
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
        }
    }
}

@Composable
fun GreetingScreen(
    userId: String,
    viewModel: GreetingViewModel = viewModel(
        factory = GreetingViewModelFactory(userId)
    )
) {
    val messageUser by viewModel.message.observeAsState("")
    Text(messageUser)
}

class GreetingViewModel(private val userId: String) : ViewModel() {
    private val _message = MutableLiveData("Hi $userId")
    val message: LiveData<String> = _message
}

class GreetingViewModelFactory(private val userId: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass) {
            GreetingViewModel::class.java -> GreetingViewModel(userId) as T
            WellnessViewModel::class.java -> WellnessViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}