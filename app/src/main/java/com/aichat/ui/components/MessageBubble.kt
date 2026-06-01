package com.aichat.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aichat.domain.model.ChatMessage
import com.aichat.domain.model.MessageRole
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun MessageBubble(
    message: ChatMessage,
    showThinking: Boolean = false
) {
    val isUser = message.role == MessageRole.USER
    val hasThinking = message.thinkingContent.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        if (hasThinking && showThinking) {
            ThinkingBlock(thinkingContent = message.thinkingContent)
            Spacer(modifier = Modifier.height(4.dp))
        }

        Box(
            modifier = Modifier
                .then(
                    if (isUser) Modifier.padding(start = 48.dp)
                    else Modifier.padding(end = 48.dp)
                )
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isUser) MiuixTheme.colorScheme.primary.copy(alpha = 0.9f)
                    else MiuixTheme.colorScheme.surfaceVariant
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                color = if (isUser) Color.White else MiuixTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun StreamingBubble(
    content: String,
    thinkingContent: String = "",
    showThinking: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        if (thinkingContent.isNotEmpty() && showThinking) {
            ThinkingBlock(thinkingContent = thinkingContent, isStreaming = true)
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (content.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(end = 48.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 4.dp,
                            bottomEnd = 16.dp
                        )
                    )
                    .background(MiuixTheme.colorScheme.surfaceVariant)
                    .padding(12.dp)
            ) {
                Text(
                    text = content,
                    color = MiuixTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun ThinkingBlock(
    thinkingContent: String,
    isStreaming: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MiuixTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { expanded = !expanded }
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (isStreaming) "思考中..." else "思考过程",
                fontSize = 12.sp,
                color = MiuixTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (expanded) "收起" else "展开",
                fontSize = 11.sp,
                color = MiuixTheme.colorScheme.primary
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = thinkingContent,
                    fontSize = 13.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    fontStyle = FontStyle.Italic
                )
            }
        }

        if (!expanded) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = thinkingContent.take(80) + if (thinkingContent.length > 80) "..." else "",
                fontSize = 12.sp,
                color = MiuixTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                maxLines = 1
            )
        }
    }
}
