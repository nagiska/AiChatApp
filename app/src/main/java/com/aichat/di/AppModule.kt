package com.aichat.di

import android.content.Context
import androidx.room.Room
import com.aichat.data.local.AppDatabase
import com.aichat.data.local.dao.ApiKeyDao
import com.aichat.data.local.dao.ConversationDao
import com.aichat.data.local.dao.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "aichat.db"
        ).build()
    }

    @Provides
    fun provideConversationDao(db: AppDatabase): ConversationDao = db.conversationDao()

    @Provides
    fun provideMessageDao(db: AppDatabase): MessageDao = db.messageDao()

    @Provides
    fun provideApiKeyDao(db: AppDatabase): ApiKeyDao = db.apiKeyDao()

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(Logging) {
                level = LogLevel.BODY
            }
            engine {
                config {
                    retryOnConnectionFailure(true)
                }
            }
        }
    }
}
