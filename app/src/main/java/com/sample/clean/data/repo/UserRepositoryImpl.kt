package com.sample.clean.data.repo

import android.util.Log
import com.sample.clean.data.api.ApiService
import com.sample.clean.data.api.model.toDomainList
import com.sample.clean.domain.repo.UserRepository
import com.sample.clean.domain.repo.model.User

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {
    override suspend fun getUsers(): List<User> {
        val userList = apiService.getUsers()
        return userList.toDomainList()
    }
}