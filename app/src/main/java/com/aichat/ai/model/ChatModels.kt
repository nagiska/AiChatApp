package com.aichat.ai.model

import kotlinx.serialization.Serializable

// 聊天消息模型
@Serializable
data class ChatMessage(
    val role: String,      // user, assistant, system
    val content: String,   // 消息内容
    val name: String? = null
)

// 聊天请求模型
@Serializable
data class ChatRequest(
    val model: String,                    // 模型名称
    val messages: List<ChatMessage>,      // 消息列表
    val temperature: Float = 0.7f,        // 温度参数
    val maxTokens: Int = 2048,            // 最大 token 数
    val stream: Boolean = false,          // 是否流式传输
    val topP: Float = 1.0f,              // Top P 参数
    val frequencyPenalty: Float = 0f,    // 频率惩罚
    val presencePenalty: Float = 0f      // 存在惩罚
)

// 聊天响应模型
@Serializable
data class ChatResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage? = null
)

// 选择模型
@Serializable
data class Choice(
    val index: Int,
    val message: ChatMessage? = null,
    val delta: Delta? = null,
    val finishReason: String? = null
)

// 增量模型（用于流式传输）
@Serializable
data class Delta(
    val role: String? = null,
    val content: String? = null
)

// 使用量模型
@Serializable
data class Usage(
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
)

// 流式响应事件
data class StreamEvent(
    val content: String,
    val isFinished: Boolean
)
