package com.aichat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiKeyConfig(
    val id: String = "",
    val provider: AIProvider = AIProvider.OPENAI,
    val apiKey: String = "",
    val baseUrl: String = "",
    val modelName: String = "",
    val isDefault: Boolean = false,
    val temperature: Float = 0.7f,
    val maxTokens: Int = 4096,
    val showThinking: Boolean = false,
    val thinkingIntensity: ThinkingIntensity = ThinkingIntensity.MEDIUM
) {
    companion object {
        fun defaultBaseUrl(provider: AIProvider): String = when (provider) {
            AIProvider.OPENAI -> "https://api.openai.com/v1"
            AIProvider.CLAUDE -> "https://api.anthropic.com/v1"
            AIProvider.DEEPSEEK -> "https://api.deepseek.com"
            AIProvider.GEMINI -> "https://generativelanguage.googleapis.com/v1beta"
            AIProvider.QWEN -> "https://dashscope.aliyuncs.com/compatible-mode/v1"
            AIProvider.ZHIPU -> "https://open.bigmodel.cn/api/paas/v4"
            AIProvider.MIMO -> "https://api.xiaomimimo.com/v1"
            AIProvider.CUSTOM -> ""
        }

        fun defaultModel(provider: AIProvider): String = when (provider) {
            AIProvider.OPENAI -> "gpt-4o"
            AIProvider.CLAUDE -> "claude-sonnet-4-20250514"
            AIProvider.DEEPSEEK -> "deepseek-v4-flash"
            AIProvider.GEMINI -> "gemini-2.0-flash"
            AIProvider.QWEN -> "qwen-plus"
            AIProvider.ZHIPU -> "glm-4-flash"
            AIProvider.MIMO -> "mimo-v2.5-pro"
            AIProvider.CUSTOM -> ""
        }

        fun thinkingModel(provider: AIProvider): String = when (provider) {
            AIProvider.DEEPSEEK -> "deepseek-v4-pro"
            AIProvider.CLAUDE -> "claude-sonnet-4-20250514"
            AIProvider.QWEN -> "qwq-plus"
            AIProvider.MIMO -> "mimo-v2.5-pro"
            else -> ""
        }

        fun supportsThinking(provider: AIProvider): Boolean = when (provider) {
            AIProvider.DEEPSEEK, AIProvider.CLAUDE, AIProvider.QWEN, AIProvider.MIMO -> true
            else -> false
        }
    }
}
