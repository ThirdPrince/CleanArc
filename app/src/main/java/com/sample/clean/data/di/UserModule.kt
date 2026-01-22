package com.sample.clean.data.di

import com.sample.clean.data.repo.UserRepositoryImpl
import com.sample.clean.domain.repo.UserRepository
import com.sample.clean.domain.usecase.GetUsersUseCase
import com.sample.clean.presentation.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userModule = module {
    // 每次请求 UserRepository 时，都会 new 一个 UserRepositoryImpl 出来
    factory<UserRepository> {
        UserRepositoryImpl(apiService = get())
    }
    factory {
        GetUsersUseCase(userRepository = get())
    }

    // 3. 注入 ViewModel (必须用 viewModel 关键字)
    viewModel {
        UserViewModel(getUsersUseCase = get())
    }
}