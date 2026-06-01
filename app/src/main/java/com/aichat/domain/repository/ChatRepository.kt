package com.aichat.domain.repository

import com.aichat.domain.model.ChatMessage
import com.aichat.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllConversations(): Flow<List<Conversation>>
    fun getMessagesByConversation(conversationId: String): Flow<List<ChatMessage>>
    suspend fun createConversation(conversation: Conversation): String
    suspend fun updateConversation(conversation: Conversation)
    suspend fun deleteConversation(conversationId: String)
    suspend fun addMessage(message: ChatMessage, conversationId: String)
    suspend fun updateMessage(message: ChatMessage)
    suspend fun clearMessages(conversationId: String)
}
