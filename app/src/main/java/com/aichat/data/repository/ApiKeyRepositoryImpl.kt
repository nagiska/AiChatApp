package com.aichat.data.repository

import com.aichat.data.local.dao.ApiKeyDao
import com.aichat.data.local.entity.ApiKeyEntity
import com.aichat.domain.model.AIProvider
import com.aichat.domain.model.ApiKeyConfig
import com.aichat.domain.model.ThinkingIntensity
import com.aichat.domain.repository.ApiKeyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ApiKeyRepositoryImpl(
    private val apiKeyDao: ApiKeyDao
) : ApiKeyRepository {

    override fun getAllConfigs(): Flow<List<ApiKeyConfig>> {
        return apiKeyDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getConfigByProvider(provider: AIProvider): Flow<ApiKeyConfig?> {
        return apiKeyDao.getByProvider(provider.name).map { it?.toDomain() }
    }

    override suspend fun saveConfig(config: ApiKeyConfig) {
        apiKeyDao.insert(config.toEntity())
    }

    override suspend fun deleteConfig(id: String) {
        apiKeyDao.deleteById(id)
    }

    override suspend fun getDefaultConfig(provider: AIProvider): ApiKeyConfig? {
        return apiKeyDao.getDefaultByProvider(provider.name)?.toDomain()
    }

    private fun ApiKeyEntity.toDomain() = ApiKeyConfig(
        id = id,
        provider = AIProvider.valueOf(provider),
        apiKey = apiKey,
        baseUrl = baseUrl,
        modelName = modelName,
        isDefault = isDefault,
        temperature = temperature,
        maxTokens = maxTokens,
        showThinking = showThinking,
        thinkingIntensity = try { ThinkingIntensity.valueOf(thinkingIntensity) } catch (_: Exception) { ThinkingIntensity.MEDIUM }
    )

    private fun ApiKeyConfig.toEntity() = ApiKeyEntity(
        id = id,
        provider = provider.name,
        apiKey = apiKey,
        baseUrl = baseUrl,
        modelName = modelName,
        isDefault = isDefault,
        temperature = temperature,
        maxTokens = maxTokens,
        showThinking = showThinking,
        thinkingIntensity = thinkingIntensity.name
    )
}
