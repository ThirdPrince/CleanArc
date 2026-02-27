package com.sample.clean.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.clean.domain.usecase.GetUsersUseCase
import com.sample.clean.presentation.state.UserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            val users = getUsersUseCase()
            users.onSuccess {
                _uiState.value = UserUiState.Success(it)
            }.onFailure {
                _uiState.value = UserUiState.Error(it.message ?: "Unknown Error")
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("UserRepository", "onClear")
    }

}