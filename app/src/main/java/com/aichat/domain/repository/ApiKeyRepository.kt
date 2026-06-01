package com.aichat.domain.repository

import com.aichat.domain.model.ApiKeyConfig
import com.aichat.domain.model.AIProvider
import kotlinx.coroutines.flow.Flow

interface ApiKeyRepository {
    fun getAllConfigs(): Flow<List<ApiKeyConfig>>
    fun getConfigByProvider(provider: AIProvider): Flow<ApiKeyConfig?>
    suspend fun saveConfig(config: ApiKeyConfig)
    suspend fun deleteConfig(id: String)
    suspend fun getDefaultConfig(provider: AIProvider): ApiKeyConfig?
}
