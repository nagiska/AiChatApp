package com.aichat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: String = "",
    val title: String = "新对话",
    val provider: AIProvider = AIProvider.OPENAI,
    val modelName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
