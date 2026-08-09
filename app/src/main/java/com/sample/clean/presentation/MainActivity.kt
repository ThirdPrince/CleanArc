package com.sample.clean.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.clean.presentation.components.ErrorView
import com.sample.clean.presentation.components.LoadingView
import com.sample.clean.presentation.components.UserList
import com.sample.clean.presentation.state.UserUiState
import com.sample.clean.presentation.theme.CleanArcTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

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
fun UserListScreen(viewModel: UserViewModel = koinViewModel()) {
    // 使用 collectAsStateWithLifecycle 安全地观察 UI 状态，它会自动感知生命周期
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
