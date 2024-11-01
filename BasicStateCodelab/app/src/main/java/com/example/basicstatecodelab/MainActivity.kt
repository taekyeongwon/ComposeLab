package com.example.basicstatecodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.basicstatecodelab.ui.theme.BasicStateCodelabTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<WellnessViewModel>(
        factoryProducer = {
            SavedStateViewModelFactory(
                application = application,
                this,
                intent?.extras
            )
        }

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BasicStateCodelabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WellnessScreen(wellnessViewModel = viewModel)
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
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}