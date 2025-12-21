package com.sample.clean.data.di

import com.sample.clean.data.repo.UserRepositoryImpl
import com.sample.clean.domain.repo.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    // 每次请求 UserRepository 时，都会 new 一个 UserRepositoryImpl 出来
    factory<UserRepository> {
        UserRepositoryImpl(apiService = get())
    }
}