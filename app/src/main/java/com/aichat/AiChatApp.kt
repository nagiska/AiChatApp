package com.aichat

import android.app.Application
import com.aichat.di.AppContainer

class AiChatApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
