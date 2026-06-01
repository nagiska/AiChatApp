package com.aichat.domain.repository

import com.aichat.data.remote.StreamResult
import com.aichat.domain.model.AIProvider
import com.aichat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface AiClientRepository {
    suspend fun sendMessage(
        provider: AIProvider,
        apiKey: String,
        baseUrl: String,
        modelName: String,
        messages: List<ChatMessage>,
        temperature: Float = 0.7f,
        maxTokens: Int = 4096,
        enableThinking: Boolean = false,
        thinkingBudget: Int = 4096
    ): Flow<StreamResult>
}
