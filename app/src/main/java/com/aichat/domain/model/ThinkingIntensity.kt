package com.aichat.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ThinkingIntensity(val displayName: String, val temperature: Float) {
    LOW("低 (精确)", 0.3f),
    MEDIUM("中 (平衡)", 0.7f),
    HIGH("高 (创造)", 1.0f)
}
