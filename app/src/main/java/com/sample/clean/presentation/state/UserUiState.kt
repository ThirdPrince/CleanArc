package com.sample.clean.presentation.state

import com.sample.clean.domain.repo.model.User

sealed interface UserUiState {
    object Loading : UserUiState
    data class Success(val users: List<User>) : UserUiState
    data class Error(val message: String) : UserUiState
}
