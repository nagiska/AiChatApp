package com.aichat.di

import com.aichat.ai.AIManager
import com.aichat.data.db.AppDatabase
import com.aichat.data.repository.*
import com.aichat.ui.screen.home.HomeViewModel
import com.aichat.ui.screen.chat.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Koin 依赖注入模块
val appModule = module {

    // 数据库
    single { AppDatabase.getDatabase(androidContext()) }

    // DAO
    single { get<AppDatabase>().conversationDao() }
    single { get<AppDatabase>().messageDao() }
    single { get<AppDatabase>().providerDao() }
    single { get<AppDatabase>().agentDao() }

    // 仓库
    single { ConversationRepository(get(), get()) }
    single { MessageRepository(get()) }
    single { ProviderRepository(get()) }
    single { AgentRepository(get()) }

    // AI 管理器
    single { AIManager() }

    // ViewModel
    viewModel { HomeViewModel(get()) }
    viewModel { (conversationId: String) -> 
        ChatViewModel(conversationId, get(), get(), get()) 
    }
}
