package com.aichat

import android.app.Application
import com.aichat.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// 应用程序入口类
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化 Koin 依赖注入
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}
