package com.aichat.data.db.dao

import androidx.room.*
import com.aichat.data.db.entity.Agent
import kotlinx.coroutines.flow.Flow

// Agent 数据访问对象
@Dao
interface AgentDao {
    
    // 获取所有 Agent
    @Query("SELECT * FROM agents ORDER BY createdAt DESC")
    fun getAllAgents(): Flow<List<Agent>>
    
    // 获取内置 Agent
    @Query("SELECT * FROM agents WHERE isBuiltIn = 1 ORDER BY createdAt DESC")
    fun getBuiltInAgents(): Flow<List<Agent>>
    
    // 获取自定义 Agent
    @Query("SELECT * FROM agents WHERE isBuiltIn = 0 ORDER BY createdAt DESC")
    fun getCustomAgents(): Flow<List<Agent>>
    
    // 根据 ID 获取 Agent
    @Query("SELECT * FROM agents WHERE id = :id")
    suspend fun getAgentById(id: String): Agent?
    
    // 插入 Agent
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: Agent)
    
    // 更新 Agent
    @Update
    suspend fun updateAgent(agent: Agent)
    
    // 删除 Agent
    @Delete
    suspend fun deleteAgent(agent: Agent)
    
    // 根据 ID 删除 Agent
    @Query("DELETE FROM agents WHERE id = :id")
    suspend fun deleteAgentById(id: String)
}
