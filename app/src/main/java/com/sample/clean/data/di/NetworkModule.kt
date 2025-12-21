package com.sample.clean.data.di

import com.sample.clean.data.api.ApiService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    // 2. 提供 OkHttpClient
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // 3. 提供 Retrofit 实例
    single {
        Retrofit.Builder()
            .baseUrl("https://5e510330f2c0d300147c034c.mockapi.io/")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 4. 提供 ApiService 实例
    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}

