package com.aichat.ai

import com.aichat.ai.model.ChatMessage
import com.aichat.ai.model.ChatResponse
import com.aichat.ai.model.StreamEvent
import com.aichat.ai.provider.AIProvider
import com.aichat.ai.provider.OpenAICompatibleProvider
import com.aichat.data.db.entity.Provider
import kotlinx.coroutines.flow.Flow

// AI 管理器 - 管理所有 AI 供应商
class AIManager {
    private val providers = mutableMapOf<String, AIProvider>()

    // 注册供应商
    fun registerProvider(provider: Provider) {
        val aiProvider = createProvider(provider)
        providers[provider.id] = aiProvider
    }

    // 创建 AI 供应商实例
    private fun createProvider(provider: Provider): AIProvider {
        val models = provider.models.split(",").map { it.trim() }
        return when (provider.type) {
            "openai", "deepseek", "qwen", "custom" -> {
                OpenAICompatibleProvider(
                    apiKey = provider.apiKey,
                    baseUrl = provider.baseUrl,
                    name = provider.name,
                    type = provider.type,
                    models = models
                )
            }
            else -> throw IllegalArgumentException("Unknown provider type: ${provider.type}")
        }
    }

    // 获取供应商
    fun getProvider(providerId: String): AIProvider? = providers[providerId]

    // 发送消息
    suspend fun sendMessage(
        providerId: String,
        messages: List<ChatMessage>,
        model: String,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048
    ): ChatResponse {
        val provider = providers[providerId]
            ?: throw IllegalArgumentException("Provider not found: $providerId")
        return provider.chat(messages, model, temperature, maxTokens)
    }

    // 发送流式消息
    fun sendMessageStream(
        providerId: String,
        messages: List<ChatMessage>,
        model: String,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048
    ): Flow<StreamEvent> {
        val provider = providers[providerId]
            ?: throw IllegalArgumentException("Provider not found: $providerId")
        return provider.chatStream(messages, model, temperature, maxTokens)
    }
}
