package com.aichat.ui.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aichat.ui.ChatViewModel
import com.aichat.ui.components.ChatInput
import com.aichat.ui.components.MessageBubble
import com.aichat.ui.components.StreamingBubble
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TopAppBar

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val conversation = uiState.currentConversation

    LaunchedEffect(uiState.messages.size, uiState.streamingContent, uiState.streamingThinking) {
        if (uiState.messages.isNotEmpty() || uiState.streamingContent.isNotEmpty()) {
            listState.animateScrollToItem(
                (uiState.messages.size + if (uiState.streamingContent.isNotEmpty() || uiState.streamingThinking.isNotEmpty()) 1 else 0) - 1
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = conversation?.title ?: "对话")
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = false
            ) {
                items(uiState.messages, key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        showThinking = uiState.showThinkingOutput
                    )
                }

                if (uiState.isStreaming && (uiState.streamingContent.isNotEmpty() || uiState.streamingThinking.isNotEmpty())) {
                    item {
                        StreamingBubble(
                            content = uiState.streamingContent,
                            thinkingContent = uiState.streamingThinking,
                            showThinking = uiState.showThinkingOutput
                        )
                    }
                }

                if (uiState.messages.isEmpty() && !uiState.isStreaming) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "发送消息开始对话",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { viewModel.toggleThinkingOutput() }) {
                    Text(
                        text = if (uiState.showThinkingOutput) "隐藏思考" else "显示思考",
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${uiState.messages.size} 条消息",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ChatInput(
                onSend = { viewModel.sendMessage(it) },
                enabled = !uiState.isStreaming
            )
        }
    }
}
