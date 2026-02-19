package com.sample.clean

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.sample.clean.presentation.UserViewModel
import com.sample.clean.ui.ErrorView
import com.sample.clean.ui.LoadingView
import com.sample.clean.ui.UserList
import com.sample.clean.ui.model.UserUiState
import com.sample.clean.ui.theme.CleanArcTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called! ViewModel should be cleared now.")
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