package com.sample.clean.domain.repo

import com.sample.clean.domain.repo.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
}