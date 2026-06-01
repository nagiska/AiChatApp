package com.aichat.di

import android.content.Context
import androidx.room.Room
import com.aichat.data.local.AppDatabase
import com.aichat.data.local.dao.ApiKeyDao
import com.aichat.data.local.dao.ConversationDao
import com.aichat.data.local.dao.MessageDao
import com.aichat.data.remote.AiClientRepositoryImpl
import com.aichat.data.remote.ClaudeClient
import com.aichat.data.remote.OpenAiClient
import com.aichat.data.repository.ApiKeyRepositoryImpl
import com.aichat.data.repository.ChatRepositoryImpl
import com.aichat.domain.repository.AiClientRepository
import com.aichat.domain.repository.ApiKeyRepository
import com.aichat.domain.repository.ChatRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

class AppContainer(context: Context) {
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "aichat.db"
    ).build()

    private val conversationDao: ConversationDao = database.conversationDao()
    private val messageDao: MessageDao = database.messageDao()
    private val apiKeyDao: ApiKeyDao = database.apiKeyDao()

    private val httpClient: HttpClient = HttpClient(OkHttp) {
        install(Logging) {
            level = LogLevel.BODY
        }
        engine {
            config {
                retryOnConnectionFailure(true)
            }
        }
    }

    private val openAiClient: OpenAiClient = OpenAiClient(httpClient)
    private val claudeClient: ClaudeClient = ClaudeClient(httpClient)

    val chatRepository: ChatRepository = ChatRepositoryImpl(conversationDao, messageDao)
    val apiKeyRepository: ApiKeyRepository = ApiKeyRepositoryImpl(apiKeyDao)
    val aiClientRepository: AiClientRepository = AiClientRepositoryImpl(openAiClient, claudeClient)
}
