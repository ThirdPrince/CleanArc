package com.sample.clean

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.sample.clean.domain.repo.UserRepository
import com.sample.clean.presentation.UserViewModel
import com.sample.clean.ui.ErrorView
import com.sample.clean.ui.LoadingView
import com.sample.clean.ui.UserList
import com.sample.clean.ui.model.UserUiState
import com.sample.clean.ui.theme.CleanArcTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    private val userRepository: UserRepository by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanArcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserListScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CleanArcTheme {
        Greeting("Android")
    }
}

@Composable
fun UserListScreen(viewModel: UserViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    when (val currentState = state) {
        is UserUiState.Loading -> {
            LoadingView()
        }

        is UserUiState.Success -> {
            UserList(users = currentState.users)
        }

        is UserUiState.Error -> {
            ErrorView(message = currentState.message)
        }
    }
}