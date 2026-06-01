package com.aichat.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class AIProvider(val displayName: String, val icon: String) {
    OPENAI("OpenAI", "🟢"),
    CLAUDE("Claude", "🟠"),
    DEEPSEEK("DeepSeek", "🔵"),
    GEMINI("Gemini", "🔴"),
    QWEN("通义千问", "🟣"),
    ZHIPU("智谱 GLM", "🟡"),
    CUSTOM("自定义", "⚪")
}
