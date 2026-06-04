package com.aichat.data.repository

import com.aichat.data.db.dao.ConversationDao
import com.aichat.data.db.dao.MessageDao
import com.aichat.data.db.entity.Conversation
import com.aichat.data.db.entity.Message
import kotlinx.coroutines.flow.Flow
import java.util.UUID

// 会话仓库 - 处理会话相关的业务逻辑
class ConversationRepository(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) {
    // 获取所有会话
    fun getAllConversations(): Flow<List<Conversation>> = 
        conversationDao.getAllConversations()

    // 获取已归档的会话
    fun getArchivedConversations(): Flow<List<Conversation>> = 
        conversationDao.getArchivedConversations()

    // 根据 ID 获取会话
    suspend fun getConversationById(id: String): Conversation? = 
        conversationDao.getConversationById(id)

    // 创建新会话
    suspend fun createConversation(
        title: String,
        providerId: String,
        model: String,
        systemPrompt: String = ""
    ): Conversation {
        val conversation = Conversation(
            id = UUID.randomUUID().toString(),
            title = title,
            providerId = providerId,
            model = model,
            systemPrompt = systemPrompt,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        conversationDao.insertConversation(conversation)
        return conversation
    }

    // 更新会话
    suspend fun updateConversation(conversation: Conversation) {
        conversationDao.updateConversation(
            conversation.copy(updatedAt = System.currentTimeMillis())
        )
    }

    // 删除会话（同时删除相关消息）
    suspend fun deleteConversation(id: String) {
        messageDao.deleteMessagesByConversationId(id)
        conversationDao.deleteConversationById(id)
    }

    // 归档会话
    suspend fun archiveConversation(id: String) {
        conversationDao.archiveConversation(id)
    }

    // 取消归档
    suspend fun unarchiveConversation(id: String) {
        conversationDao.unarchiveConversation(id)
    }

    // 搜索会话
    fun searchConversations(query: String): Flow<List<Conversation>> = 
        conversationDao.searchConversations(query)
}
