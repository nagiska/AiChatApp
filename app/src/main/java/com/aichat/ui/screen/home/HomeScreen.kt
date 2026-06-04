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
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

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
                    icon = Icons.Default.Chat,
                    label = "会话"
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { 
                        selectedTab = 1
                        onNavigateToAgents()
                    },
                    icon = Icons.Default.SmartToy,
                    label = "Agent"
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { 
                        selectedTab = 2
                        onNavigateToSettings()
                    },
                    icon = Icons.Default.Settings,
                    label = "设置"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ConversationList(
                onConversationClick = { conversationId ->
                    onNavigateToChat(conversationId)
                }
            )

            if (showNewChatDialog) {
                NewChatDialog(
                    onDismiss = { showNewChatDialog = false },
                    onCreate = { title, providerId, model ->
                        showNewChatDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ConversationList(
    onConversationClick: (String) -> Unit
) {
    val conversations = remember { emptyList<String>() }

    if (conversations.isEmpty()) {
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

@Composable
private fun NewChatDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    OverlayDialog(
        title = "新建会话",
        show = true,
        onDismissRequest = onDismiss
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("会话标题") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
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
