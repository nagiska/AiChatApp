package com.aichat.data.repository

import com.aichat.data.db.dao.MessageDao
import com.aichat.data.db.entity.Message
import kotlinx.coroutines.flow.Flow
import java.util.UUID

// 消息仓库 - 处理消息相关的业务逻辑
class MessageRepository(
    private val messageDao: MessageDao
) {
    // 获取会话的所有消息
    fun getMessagesByConversationId(conversationId: String): Flow<List<Message>> = 
        messageDao.getMessagesByConversationId(conversationId)

    // 获取会话的最新消息
    suspend fun getLatestMessage(conversationId: String): Message? = 
        messageDao.getLatestMessage(conversationId)

    // 根据 ID 获取消息
    suspend fun getMessageById(id: String): Message? = 
        messageDao.getMessageById(id)

    // 获取消息的子消息
    fun getChildMessages(parentId: String): Flow<List<Message>> = 
        messageDao.getChildMessages(parentId)

    // 发送用户消息
    suspend fun sendUserMessage(
        conversationId: String,
        content: String,
        parentId: String? = null
    ): Message {
        val message = Message(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            role = "user",
            content = content,
            createdAt = System.currentTimeMillis(),
            parentId = parentId
        )
        messageDao.insertMessage(message)
        return message
    }

    // 保存 AI 回复
    suspend fun saveAssistantMessage(
        conversationId: String,
        content: String,
        parentId: String? = null
    ): Message {
        val message = Message(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            role = "assistant",
            content = content,
            createdAt = System.currentTimeMillis(),
            parentId = parentId
        )
        messageDao.insertMessage(message)
        return message
    }

    // 更新消息内容（用于流式传输）
    suspend fun updateMessageContent(messageId: String, content: String) {
        messageDao.getMessageById(messageId)?.let { message ->
            messageDao.updateMessage(message.copy(content = content))
        }
    }

    // 删除消息
    suspend fun deleteMessage(message: Message) {
        messageDao.deleteMessage(message)
    }

    // 搜索消息
    fun searchMessages(query: String): Flow<List<Message>> = 
        messageDao.searchMessages(query)
}
