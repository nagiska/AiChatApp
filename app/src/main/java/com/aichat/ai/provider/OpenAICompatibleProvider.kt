package com.aichat.ai.provider

import com.aichat.ai.model.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

// OpenAI 兼容的 AI 供应商实现
// 支持 OpenAI、DeepSeek、通义千问等兼容 OpenAI 格式的 API
class OpenAICompatibleProvider(
    private val apiKey: String,
    private val baseUrl: String,
    override val name: String = "OpenAI",
    override val type: String = "openai",
    override val models: List<String> = listOf("gpt-4o", "gpt-4", "gpt-3.5-turbo")
) : AIProvider {

    private val client = HttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    // 非流式聊天
    override suspend fun chat(
        messages: List<ChatMessage>,
        model: String,
        temperature: Float,
        maxTokens: Int
    ): ChatResponse {
        val request = ChatRequest(
            model = model,
            messages = messages,
            temperature = temperature,
            maxTokens = maxTokens,
            stream = false
        )

        val response = client.post("$baseUrl/chat/completions") {
            header("Authorization", "Bearer $apiKey")
            contentType(ContentType.Application.Json)
            setBody(json.encodeToString(ChatRequest.serializer(), request))
        }

        return json.decodeFromString(ChatResponse.serializer(), response.bodyAsText())
    }

    // 流式聊天
    override fun chatStream(
        messages: List<ChatMessage>,
        model: String,
        temperature: Float,
        maxTokens: Int
    ): Flow<StreamEvent> = flow {
        val request = ChatRequest(
            model = model,
            messages = messages,
            temperature = temperature,
            maxTokens = maxTokens,
            stream = true
        )

        val response = client.post("$baseUrl/chat/completions") {
            header("Authorization", "Bearer $apiKey")
            contentType(ContentType.Application.Json)
            setBody(json.encodeToString(ChatRequest.serializer(), request))
        }

        val responseBody = response.bodyAsText()
        val lines = responseBody.split("\n")

        for (line in lines) {
            if (line.startsWith("data: ")) {
                val data = line.removePrefix("data: ").trim()
                if (data == "[DONE]") {
                    emit(StreamEvent(content = "", isFinished = true))
                    break
                }

                try {
                    val chunk = json.decodeFromString(ChatResponse.serializer(), data)
                    val content = chunk.choices.firstOrNull()?.delta?.content ?: ""
                    if (content.isNotEmpty()) {
                        emit(StreamEvent(content = content, isFinished = false))
                    }
                } catch (e: Exception) {
                    // 忽略解析错误
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}
