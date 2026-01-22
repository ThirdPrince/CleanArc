package com.sample.clean.app

import android.app.Application
import com.sample.clean.data.di.networkModule
import com.sample.clean.data.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CleanApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CleanApplication)
            // 加载所有相关模块
            modules(listOf(
                networkModule,
                userModule,
            ))
        }
    }
}