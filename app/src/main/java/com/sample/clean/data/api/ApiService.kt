package com.sample.clean.data.api

import com.sample.clean.data.api.model.ApiUser
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<ApiUser>

}