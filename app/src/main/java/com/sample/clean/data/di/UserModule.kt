package com.sample.clean.data.di

import com.sample.clean.data.repo.UserRepositoryImpl
import com.sample.clean.domain.repo.UserRepository
import com.sample.clean.domain.usecase.GetUsersUseCase
import com.sample.clean.presentation.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {

    factory<UserRepository> {
        UserRepositoryImpl(apiService = get())
    }
    factory {
        GetUsersUseCase(userRepository = get())
    }

    viewModel {
        UserViewModel(getUsersUseCase = get())
    }
}