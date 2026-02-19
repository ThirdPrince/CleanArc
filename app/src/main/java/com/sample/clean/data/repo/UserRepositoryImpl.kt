package com.sample.clean.data.repo

import android.util.Log
import com.sample.clean.data.api.ApiService
import com.sample.clean.data.api.model.toDomainList
import com.sample.clean.domain.repo.UserRepository
import com.sample.clean.domain.repo.model.User
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {
    // In UserRepositoryImpl.kt
    // In UserRepositoryImpl.kt
    override suspend fun getUsers(): Result<List<User>> {
        Log.d("UserRepository", "开始请求用户数据...")
        return try {
            delay(3000)
            val userList = apiService.getUsers()
            Log.d("UserRepository", "请求成功")

            Result.success(userList.toDomainList())
        } catch (e: Exception) {
            Log.e("UserRepository", "请求因异常失败", e)
            Result.failure(e)
        }
    }
}