package com.aichat.data.repository

import com.aichat.data.db.dao.ProviderDao
import com.aichat.data.db.entity.Provider
import kotlinx.coroutines.flow.Flow
import java.util.UUID

// 供应商仓库 - 处理供应商相关的业务逻辑
class ProviderRepository(
    private val providerDao: ProviderDao
) {
    // 获取所有供应商
    fun getAllProviders(): Flow<List<Provider>> = 
        providerDao.getAllProviders()

    // 获取启用的供应商
    fun getEnabledProviders(): Flow<List<Provider>> = 
        providerDao.getEnabledProviders()

    // 根据 ID 获取供应商
    suspend fun getProviderById(id: String): Provider? = 
        providerDao.getProviderById(id)

    // 创建新供应商
    suspend fun createProvider(
        name: String,
        type: String,
        apiKey: String,
        baseUrl: String,
        models: String
    ): Provider {
        val provider = Provider(
            id = UUID.randomUUID().toString(),
            name = name,
            type = type,
            apiKey = apiKey,
            baseUrl = baseUrl,
            models = models,
            createdAt = System.currentTimeMillis()
        )
        providerDao.insertProvider(provider)
        return provider
    }

    // 更新供应商
    suspend fun updateProvider(provider: Provider) {
        providerDao.updateProvider(provider)
    }

    // 删除供应商
    suspend fun deleteProvider(id: String) {
        providerDao.deleteProviderById(id)
    }
}
