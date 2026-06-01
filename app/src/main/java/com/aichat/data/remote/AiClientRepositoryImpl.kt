package com.aichat.data.remote

import com.aichat.domain.model.AIProvider
import com.aichat.domain.model.ChatMessage
import com.aichat.domain.repository.AiClientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiClientRepositoryImpl @Inject constructor(
    private val openAiClient: OpenAiClient,
    private val claudeClient: ClaudeClient
) : AiClientRepository {

    override suspend fun sendMessage(
        provider: AIProvider,
        apiKey: String,
        baseUrl: String,
        modelName: String,
        messages: List<ChatMessage>,
        temperature: Float,
        maxTokens: Int,
        enableThinking: Boolean,
        thinkingBudget: Int,
        reasoningEffort: String
    ): Flow<StreamResult> {
        return when (provider) {
            AIProvider.CLAUDE -> {
                claudeClient.sendMessage(
                    apiKey = apiKey,
                    baseUrl = baseUrl,
                    modelName = modelName,
                    messages = messages,
                    temperature = temperature,
                    maxTokens = maxTokens,
                    enableThinking = enableThinking,
                    thinkingBudget = thinkingBudget
                )
            }
            else -> {
                openAiClient.sendMessage(
                    apiKey = apiKey,
                    baseUrl = baseUrl,
                    modelName = modelName,
                    messages = messages,
                    temperature = temperature,
                    maxTokens = maxTokens,
                    enableThinking = enableThinking,
                    reasoningEffort = reasoningEffort
                )
            }
        }
    }
}
