package com.aichat.data.db.dao

import androidx.room.*
import com.aichat.data.db.entity.Message
import kotlinx.coroutines.flow.Flow

// 消息数据访问对象
@Dao
interface MessageDao {
    
    // 获取会话的所有消息（按创建时间排序）
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY createdAt ASC")
    fun getMessagesByConversationId(conversationId: String): Flow<List<Message>>
    
    // 获取会话的最新消息
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestMessage(conversationId: String): Message?
    
    // 根据 ID 获取消息
    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getMessageById(id: String): Message?
    
    // 获取消息的子消息（用于消息分支）
    @Query("SELECT * FROM messages WHERE parentId = :parentId ORDER BY createdAt ASC")
    fun getChildMessages(parentId: String): Flow<List<Message>>
    
    // 插入消息
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)
    
    // 批量插入消息
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Message>)
    
    // 更新消息
    @Update
    suspend fun updateMessage(message: Message)
    
    // 删除消息
    @Delete
    suspend fun deleteMessage(message: Message)
    
    // 删除会话的所有消息
    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesByConversationId(conversationId: String)
    
    // 搜索消息
    @Query("SELECT * FROM messages WHERE content LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchMessages(query: String): Flow<List<Message>>
}
