package com.aichat.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val model: String,
    val messages: List<ChatRequestMessage>,
    val stream: Boolean = true,
    val temperature: Double = 0.7,
    @SerialName("max_tokens") val maxTokens: Int = 4096,
    val thinking: ThinkingConfig? = null,
    @SerialName("reasoning_effort") val reasoningEffort: String? = null
)

@Serializable
data class ThinkingConfig(
    val type: String = "enabled"
)

@Serializable
data class ChatRequestMessage(
    val role: String,
    val content: String
)

@Serializable
data class ChatCompletionResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val delta: Delta? = null,
    val message: ChatRequestMessage? = null
)

@Serializable
data class Delta(
    val content: String? = null,
    @SerialName("reasoning_content") val reasoningContent: String? = null
)
