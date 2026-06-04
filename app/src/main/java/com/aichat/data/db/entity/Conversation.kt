package com.aichat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 会话实体 - 存储每个聊天对话
@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey
    val id: String,                    // 唯一标识
    val title: String,                 // 会话标题
    val providerId: String,            // 使用的 AI 供应商 ID
    val model: String,                 // 使用的模型名称
    val systemPrompt: String = "",     // 系统提示词
    val temperature: Float = 0.7f,     // 温度参数
    val createdAt: Long,               // 创建时间
    val updatedAt: Long,               // 更新时间
    val isArchived: Boolean = false    // 是否归档
)
