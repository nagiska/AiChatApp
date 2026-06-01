package com.aichat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_keys")
data class ApiKeyEntity(
    @PrimaryKey val id: String,
    val provider: String,
    val apiKey: String,
    val baseUrl: String,
    val modelName: String,
    val isDefault: Boolean,
    val temperature: Float = 0.7f,
    val maxTokens: Int = 4096,
    val showThinking: Boolean = false,
    val thinkingIntensity: String = "MEDIUM"
)
