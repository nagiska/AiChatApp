package com.aichat.ui.navigation

// 导航路由定义
sealed class Screen(val route: String) {
    // 首页（会话列表）
    object Home : Screen("home")
    
    // 聊天页面
    object Chat : Screen("chat/{conversationId}") {
        fun createRoute(conversationId: String) = "chat/$conversationId"
    }
    
    // 设置页面
    object Settings : Screen("settings")
    
    // 供应商管理页面
    object Providers : Screen("providers")
    
    // 供应商编辑页面
    object ProviderEdit : Screen("provider/edit/{providerId}") {
        fun createRoute(providerId: String) = "provider/edit/$providerId"
        fun createNew() = "provider/edit/new"
    }
    
    // Agent 管理页面
    object Agents : Screen("agents")
    
    // Agent 编辑页面
    object AgentEdit : Screen("agent/edit/{agentId}") {
        fun createRoute(agentId: String) = "agent/edit/$agentId"
        fun createNew() = "agent/edit/new"
    }
}
