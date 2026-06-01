package com.aichat.di

import com.aichat.data.remote.AiClientRepositoryImpl
import com.aichat.data.remote.ClaudeClient
import com.aichat.data.remote.OpenAiClient
import com.aichat.data.repository.ApiKeyRepositoryImpl
import com.aichat.data.repository.ChatRepositoryImpl
import com.aichat.domain.repository.AiClientRepository
import com.aichat.domain.repository.ApiKeyRepository
import com.aichat.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindApiKeyRepository(impl: ApiKeyRepositoryImpl): ApiKeyRepository

    @Binds
    @Singleton
    abstract fun bindAiClientRepository(impl: AiClientRepositoryImpl): AiClientRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {

    @Provides
    @Singleton
    fun provideOpenAiClient(httpClient: HttpClient): OpenAiClient {
        return OpenAiClient(httpClient)
    }

    @Provides
    @Singleton
    fun provideClaudeClient(httpClient: HttpClient): ClaudeClient {
        return ClaudeClient(httpClient)
    }
}
