package com.aichat.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// 消息实体 - 存储每条聊天消息
@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Conversation::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE  // 删除会话时自动删除相关消息
        )
    ],
    indices = [Index("conversationId")]  // 为 conversationId 创建索引
)
data class Message(
    @PrimaryKey
    val id: String,                    // 唯一标识
    val conversationId: String,        // 所属会话 ID
    val role: String,                  // 角色: user, assistant, system
    val content: String,               // 消息内容
    val createdAt: Long,               // 创建时间
    val parentId: String? = null,      // 父消息 ID (用于消息分支)
    val isStreaming: Boolean = false   // 是否正在流式传输
)
