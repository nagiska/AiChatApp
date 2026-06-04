package com.aichat.ai.provider

import com.aichat.ai.model.ChatMessage
import com.aichat.ai.model.ChatRequest
import com.aichat.ai.model.ChatResponse
import com.aichat.ai.model.StreamEvent
import kotlinx.coroutines.flow.Flow

// AI 供应商接口
interface AIProvider {
    // 供应商名称
    val name: String
    
    // 供应商类型
    val type: String
    
    // 支持的模型列表
    val models: List<String>
    
    // 发送聊天请求（非流式）
    suspend fun chat(
        messages: List<ChatMessage>,
        model: String,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048
    ): ChatResponse
    
    // 发送聊天请求（流式）
    fun chatStream(
        messages: List<ChatMessage>,
        model: String,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048
    ): Flow<StreamEvent>
}
