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

    private val TAG = "MainActivityLifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate CALLED")
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

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed CALLED. Is finishing: $isFinishing")
        super.onBackPressed() // Call the default behavior
        Log.d(TAG, "super.onBackPressed() executed. Is finishing now: $isFinishing")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause CALLED")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop CALLED")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy CALLED - THIS IS WHAT WE EXPECT")
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