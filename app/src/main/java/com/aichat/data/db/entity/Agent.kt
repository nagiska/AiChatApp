package com.aichat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Agent 实体 - 存储自定义 AI 助手配置
@Entity(tableName = "agents")
data class Agent(
    @PrimaryKey
    val id: String,                    // 唯一标识
    val name: String,                  // Agent 名称
    val icon: String,                  // 图标
    val description: String,           // 描述
    val systemPrompt: String,          // 系统提示词
    val providerId: String,            // 使用的供应商 ID
    val model: String,                 // 使用的模型
    val temperature: Float = 0.7f,     // 温度参数
    val maxTokens: Int = 2048,         // 最大 token 数
    val isBuiltIn: Boolean = false,    // 是否内置
    val createdAt: Long                // 创建时间
)
