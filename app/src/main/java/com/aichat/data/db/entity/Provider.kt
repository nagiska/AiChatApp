package com.aichat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// AI 供应商实体 - 存储 API 配置
@Entity(tableName = "providers")
data class Provider(
    @PrimaryKey
    val id: String,                    // 唯一标识
    val name: String,                  // 供应商名称
    val type: String,                  // 类型: openai, google, anthropic, custom
    val apiKey: String,                // API 密钥
    val baseUrl: String,               // API 基础 URL
    val models: String,                // 支持的模型列表 (JSON 格式)
    val isEnabled: Boolean = true,     // 是否启用
    val createdAt: Long                // 创建时间
)
