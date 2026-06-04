package com.aichat.ui.screen.agent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

// Agent 管理页面
@Composable
fun AgentScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChat: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agent") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: 添加 Agent */ }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "添加 Agent"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 内置 Agent
            item {
                Text(
                    text = "内置 Agent",
                    style = MiuixTheme.textStyles.headline1
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(getBuiltInAgents()) { agent ->
                AgentItem(
                    name = agent.name,
                    description = agent.description,
                    icon = agent.icon,
                    onClick = { onNavigateToChat(agent.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "自定义 Agent",
                    style = MiuixTheme.textStyles.headline1
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 自定义 Agent 列表
            items(emptyList<AgentItem>()) { agent ->
                AgentItem(
                    name = agent.name,
                    description = agent.description,
                    icon = agent.icon,
                    onClick = { onNavigateToChat(agent.id) }
                )
            }
        }
    }
}

// Agent 列表项
@Composable
private fun AgentItem(
    name: String,
    description: String,
    icon: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 图标
            Icon(
                imageVector = when (icon) {
                    "translate" -> Icons.Default.Translate
                    "code" -> Icons.Default.Code
                    "write" -> Icons.Default.Edit
                    "search" -> Icons.Default.Search
                    else -> Icons.Default.SmartToy
                },
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MiuixTheme.textStyles.headline1
                )
                Text(
                    text = description,
                    style = MiuixTheme.textStyles.body2,
                    maxLines = 2
                )
            }

            // 箭头
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
    }
}

// Agent 数据类
private data class AgentItem(
    val id: String,
    val name: String,
    val description: String,
    val icon: String
)

// 获取内置 Agent 列表
private fun getBuiltInAgents(): List<AgentItem> = listOf(
    AgentItem("translate", "翻译助手", "专业翻译，支持多种语言互译", "translate"),
    AgentItem("code", "代码助手", "帮助编写、调试和优化代码", "code"),
    AgentItem("write", "写作助手", "帮助撰写文章、邮件、报告等", "write"),
    AgentItem("search", "搜索助手", "帮助搜索和整理信息", "search"),
    AgentItem("general", "通用助手", "通用 AI 助手，回答各种问题", "general")
)
