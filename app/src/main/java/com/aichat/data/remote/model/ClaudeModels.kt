package com.aichat.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClaudeRequest(
    val model: String,
    @SerialName("max_tokens") val maxTokens: Int = 4096,
    val messages: List<ClaudeMessage>,
    val stream: Boolean = true,
    val temperature: Double = 0.7,
    val thinking: ClaudeThinking? = null
)

@Serializable
data class ClaudeThinking(
    val type: String = "enabled",
    @SerialName("budget_tokens") val budgetTokens: Int = 4096
)

@Serializable
data class ClaudeMessage(
    val role: String,
    val content: String
)

@Serializable
data class ClaudeStreamResponse(
    val type: String,
    val delta: ClaudeDelta? = null
)

@Serializable
data class ClaudeDelta(
    val type: String? = null,
    val text: String? = null,
    val thinking: String? = null
)
