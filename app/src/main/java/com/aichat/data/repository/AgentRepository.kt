package com.aichat.data.repository

import com.aichat.data.db.dao.AgentDao
import com.aichat.data.db.entity.Agent
import kotlinx.coroutines.flow.Flow
import java.util.UUID

// Agent 仓库 - 处理 Agent 相关的业务逻辑
class AgentRepository(
    private val agentDao: AgentDao
) {
    // 获取所有 Agent
    fun getAllAgents(): Flow<List<Agent>> = 
        agentDao.getAllAgents()

    // 获取内置 Agent
    fun getBuiltInAgents(): Flow<List<Agent>> = 
        agentDao.getBuiltInAgents()

    // 获取自定义 Agent
    fun getCustomAgents(): Flow<List<Agent>> = 
        agentDao.getCustomAgents()

    // 根据 ID 获取 Agent
    suspend fun getAgentById(id: String): Agent? = 
        agentDao.getAgentById(id)

    // 创建新 Agent
    suspend fun createAgent(
        name: String,
        icon: String,
        description: String,
        systemPrompt: String,
        providerId: String,
        model: String,
        temperature: Float = 0.7f,
        maxTokens: Int = 2048
    ): Agent {
        val agent = Agent(
            id = UUID.randomUUID().toString(),
            name = name,
            icon = icon,
            description = description,
            systemPrompt = systemPrompt,
            providerId = providerId,
            model = model,
            temperature = temperature,
            maxTokens = maxTokens,
            createdAt = System.currentTimeMillis()
        )
        agentDao.insertAgent(agent)
        return agent
    }

    // 更新 Agent
    suspend fun updateAgent(agent: Agent) {
        agentDao.updateAgent(agent)
    }

    // 删除 Agent
    suspend fun deleteAgent(id: String) {
        agentDao.deleteAgentById(id)
    }
}
