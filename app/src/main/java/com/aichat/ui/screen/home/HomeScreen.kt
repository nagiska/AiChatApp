package com.aichat.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

// 首页 - 会话列表
@Composable
fun HomeScreen(
    onNavigateToChat: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAgents: () -> Unit
) {
    var showNewChatDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Chat") },
                actions = {
                    IconButton(onClick = { showNewChatDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "新建会话"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Chat, "会话") },
                    label = { Text("会话") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { 
                        selectedTab = 1
                        onNavigateToAgents()
                    },
                    icon = { Icon(Icons.Default.SmartToy, "Agent") },
                    label = { Text("Agent") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { 
                        selectedTab = 2
                        onNavigateToSettings()
                    },
                    icon = { Icon(Icons.Default.Settings, "设置") },
                    label = { Text("设置") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 会话列表内容
            ConversationList(
                onConversationClick = { conversationId ->
                    onNavigateToChat(conversationId)
                }
            )

            // 新建会话对话框
            if (showNewChatDialog) {
                NewChatDialog(
                    onDismiss = { showNewChatDialog = false },
                    onCreate = { title, providerId, model ->
                        // TODO: 创建新会话
                        showNewChatDialog = false
                    }
                )
            }
        }
    }
}

// 会话列表组件
@Composable
private fun ConversationList(
    onConversationClick: (String) -> Unit
) {
    // TODO: 从 ViewModel 获取会话列表
    val conversations = remember { emptyList<String>() }

    if (conversations.isEmpty()) {
        // 空状态
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MiuixTheme.colorScheme.onSurfaceVariantSummary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "暂无会话",
                    style = MiuixTheme.textStyles.title3
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "点击右上角 + 开始新对话",
                    style = MiuixTheme.textStyles.body2
                )
            }
        }
    } else {
        // 会话列表
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(conversations) { conversation ->
                ConversationItem(
                    conversation = conversation,
                    onClick = { onNavigateToChat(conversation) }
                )
            }
        }
    }
}

// 会话列表项组件
@Composable
private fun ConversationItem(
    conversation: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "会话标题",
                style = MiuixTheme.textStyles.headline1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "最后一条消息预览...",
                style = MiuixTheme.textStyles.body2,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "2 分钟前",
                style = MiuixTheme.textStyles.footnote1
            )
        }
    }
}

// 新建会话对话框
@Composable
private fun NewChatDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    OverlayDialog(
        visible = remember { mutableStateOf(true) },
        onDismissRequest = onDismiss
    ) {
        Card {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "新建会话",
                    style = MiuixTheme.textStyles.title2
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("会话标题") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss
                    ) {
                        Text("取消")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onCreate(title, "", "") }
                    ) {
                        Text("创建")
                    }
                }
            }
        }
    }
}
