package com.aichat.data.db.dao

import androidx.room.*
import com.aichat.data.db.entity.Conversation
import kotlinx.coroutines.flow.Flow

// 会话数据访问对象
@Dao
interface ConversationDao {
    
    // 获取所有会话（按更新时间倒序）
    @Query("SELECT * FROM conversations WHERE isArchived = 0 ORDER BY updatedAt DESC")
    fun getAllConversations(): Flow<List<Conversation>>
    
    // 获取已归档的会话
    @Query("SELECT * FROM conversations WHERE isArchived = 1 ORDER BY updatedAt DESC")
    fun getArchivedConversations(): Flow<List<Conversation>>
    
    // 根据 ID 获取会话
    @Query("SELECT * FROM conversations WHERE id = :id")
    suspend fun getConversationById(id: String): Conversation?
    
    // 插入会话
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)
    
    // 更新会话
    @Update
    suspend fun updateConversation(conversation: Conversation)
    
    // 删除会话
    @Delete
    suspend fun deleteConversation(conversation: Conversation)
    
    // 根据 ID 删除会话
    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversationById(id: String)
    
    // 归档会话
    @Query("UPDATE conversations SET isArchived = 1 WHERE id = :id")
    suspend fun archiveConversation(id: String)
    
    // 取消归档
    @Query("UPDATE conversations SET isArchived = 0 WHERE id = :id")
    suspend fun unarchiveConversation(id: String)
    
    // 搜索会话
    @Query("SELECT * FROM conversations WHERE title LIKE '%' || :query || '%' AND isArchived = 0 ORDER BY updatedAt DESC")
    fun searchConversations(query: String): Flow<List<Conversation>>
}
