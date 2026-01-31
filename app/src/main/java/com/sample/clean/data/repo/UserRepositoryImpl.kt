package com.sample.clean.data.repo

import android.util.Log
import com.sample.clean.data.api.ApiService
import com.sample.clean.data.api.model.toDomainList
import com.sample.clean.domain.repo.UserRepository
import com.sample.clean.domain.repo.model.User

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {
    override suspend fun getUsers(): Result<List<User>> {
        try {
            val userList = apiService.getUsers()
            return Result.success(userList.toDomainList())
        } catch (e: Exception) {
           return Result.failure(e)
        }
    }
}