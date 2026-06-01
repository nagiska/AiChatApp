package com.aichat.data.remote

import com.aichat.data.remote.model.ClaudeMessage
import com.aichat.data.remote.model.ClaudeRequest
import com.aichat.data.remote.model.ClaudeStreamResponse
import com.aichat.data.remote.model.ClaudeThinking
import com.aichat.domain.model.ChatMessage
import com.aichat.domain.model.MessageRole
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class ClaudeClient(private val httpClient: HttpClient) {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun sendMessage(
        apiKey: String,
        baseUrl: String,
        modelName: String,
        messages: List<ChatMessage>,
        temperature: Float = 0.7f,
        maxTokens: Int = 4096,
        enableThinking: Boolean = false,
        thinkingBudget: Int = 4096
    ): Flow<StreamResult> = flow {
        val request = ClaudeRequest(
            model = modelName,
            messages = messages.map {
                ClaudeMessage(
                    role = when (it.role) {
                        MessageRole.USER -> "user"
                        MessageRole.ASSISTANT -> "assistant"
                        MessageRole.SYSTEM -> "user"
                    },
                    content = it.content
                )
            },
            temperature = if (enableThinking) 1.0 else temperature.toDouble(),
            maxTokens = maxTokens,
            thinking = if (enableThinking) ClaudeThinking(
                type = "enabled",
                budgetTokens = thinkingBudget
            ) else null
        )

        val url = "$baseUrl/messages"
        val response = httpClient.post(url) {
            header("x-api-key", apiKey)
            header("anthropic-version", "2023-06-01")
            if (enableThinking) {
                header("anthropic-beta", "extended-thinking-2025-04-11")
            }
            contentType(ContentType.Application.Json)
            setBody(json.encodeToString(ClaudeRequest.serializer(), request))
        }

        val channel = response.bodyAsChannel()
        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: continue
            if (line.startsWith("data: ")) {
                val data = line.removePrefix("data: ").trim()
                try {
                    val chunk = json.decodeFromString(ClaudeStreamResponse.serializer(), data)
                    when (chunk.type) {
                        "content_block_delta" -> {
                            val text = chunk.delta?.text
                            val thinking = chunk.delta?.thinking
                            if (!text.isNullOrEmpty() || !thinking.isNullOrEmpty()) {
                                emit(StreamResult(
                                    content = text ?: "",
                                    thinkingContent = thinking ?: ""
                                ))
                            }
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
    }
}
