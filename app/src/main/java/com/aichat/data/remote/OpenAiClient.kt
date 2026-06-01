package com.aichat.data.remote

import com.aichat.data.remote.model.ChatRequest
import com.aichat.data.remote.model.ChatRequestMessage
import com.aichat.data.remote.model.ThinkingConfig
import com.aichat.domain.model.AIProvider
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

data class StreamResult(
    val content: String = "",
    val thinkingContent: String = ""
)

class OpenAiClient(private val httpClient: HttpClient) {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun sendMessage(
        apiKey: String,
        baseUrl: String,
        modelName: String,
        messages: List<ChatMessage>,
        provider: AIProvider,
        temperature: Float = 0.7f,
        maxTokens: Int = 4096,
        enableThinking: Boolean = false,
        reasoningEffort: String = "medium"
    ): Flow<StreamResult> = flow {
        val isMiMo = provider == AIProvider.MIMO
        val isDeepSeek = provider == AIProvider.DEEPSEEK

        val dsReasoningEffort = when {
            !isDeepSeek -> null
            reasoningEffort in listOf("low", "medium") -> "high"
            reasoningEffort == "high" -> "high"
            else -> "max"
        }

        val request = ChatRequest(
            model = modelName,
            messages = messages.map {
                ChatRequestMessage(
                    role = when (it.role) {
                        MessageRole.USER -> "user"
                        MessageRole.ASSISTANT -> "assistant"
                        MessageRole.SYSTEM -> "system"
                    },
                    content = it.content
                )
            },
            temperature = if (enableThinking && isDeepSeek) null else if (enableThinking) 1.0 else temperature.toDouble(),
            maxTokens = if (isMiMo) null else maxTokens,
            maxCompletionTokens = if (isMiMo) maxTokens else null,
            topP = if (isMiMo) 0.95 else null,
            thinking = if (enableThinking) ThinkingConfig(type = "enabled") else null,
            reasoningEffort = if (enableThinking) (dsReasoningEffort ?: reasoningEffort) else null
        )

        val url = "$baseUrl/chat/completions"
        val response = httpClient.post(url) {
            header("Authorization", "Bearer $apiKey")
            contentType(ContentType.Application.Json)
            setBody(json.encodeToString(ChatRequest.serializer(), request))
        }

        val channel = response.bodyAsChannel()
        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: continue
            if (line.startsWith("data: ")) {
                val data = line.removePrefix("data: ").trim()
                if (data == "[DONE]") break
                try {
                    val chunk = json.decodeFromString(
                        com.aichat.data.remote.model.ChatCompletionResponse.serializer(),
                        data
                    )
                    val delta = chunk.choices.firstOrNull()?.delta
                    val content = delta?.content
                    val reasoning = delta?.reasoningContent
                    if (!content.isNullOrEmpty() || !reasoning.isNullOrEmpty()) {
                        emit(StreamResult(
                            content = content ?: "",
                            thinkingContent = reasoning ?: ""
                        ))
                    }
                } catch (_: Exception) {
                }
            }
        }
    }
}
