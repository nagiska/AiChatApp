package com.aichat.data.repository

import com.aichat.data.local.dao.ConversationDao
import com.aichat.data.local.dao.MessageDao
import com.aichat.data.local.entity.ConversationEntity
import com.aichat.data.local.entity.MessageEntity
import com.aichat.domain.model.ChatMessage
import com.aichat.domain.model.Conversation
import com.aichat.domain.model.MessageRole
import com.aichat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) : ChatRepository {

    override fun getAllConversations(): Flow<List<Conversation>> {
        return conversationDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMessagesByConversation(conversationId: String): Flow<List<ChatMessage>> {
        return messageDao.getByConversationId(conversationId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun createConversation(conversation: Conversation): String {
        conversationDao.insert(conversation.toEntity())
        return conversation.id
    }

    override suspend fun updateConversation(conversation: Conversation) {
        conversationDao.update(conversation.toEntity())
    }

    override suspend fun deleteConversation(conversationId: String) {
        conversationDao.deleteById(conversationId)
    }

    override suspend fun addMessage(message: ChatMessage, conversationId: String) {
        messageDao.insert(message.toEntity(conversationId))
    }

    override suspend fun updateMessage(message: ChatMessage) {
        messageDao.update(message.toEntity(""))
    }

    override suspend fun clearMessages(conversationId: String) {
        messageDao.deleteByConversationId(conversationId)
    }

    private fun ConversationEntity.toDomain() = Conversation(
        id = id,
        title = title,
        provider = com.aichat.domain.model.AIProvider.valueOf(provider),
        modelName = modelName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Conversation.toEntity() = ConversationEntity(
        id = id,
        title = title,
        provider = provider.name,
        modelName = modelName,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun MessageEntity.toDomain() = ChatMessage(
        id = id,
        role = MessageRole.valueOf(role),
        content = content,
        thinkingContent = thinkingContent,
        timestamp = timestamp
    )

    private fun ChatMessage.toEntity(conversationId: String) = MessageEntity(
        id = id,
        conversationId = conversationId,
        role = role.name,
        content = content,
        thinkingContent = thinkingContent,
        timestamp = timestamp
    )
}
