package com.aichat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String = "",
    val role: MessageRole = MessageRole.USER,
    val content: String = "",
    val thinkingContent: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isStreaming: Boolean = false
)
