package com.aichat.ui.screen.provider

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

// 供应商管理页面
@Composable
fun ProviderScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String?) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI 供应商") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(null) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "添加供应商"
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
            // 内置供应商
            item {
                Text(
                    text = "内置供应商",
                    style = MiuixTheme.textStyles.headline1
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(getBuiltInProviders()) { provider ->
                ProviderItem(
                    name = provider.name,
                    type = provider.type,
                    isConfigured = provider.isConfigured,
                    onClick = { onNavigateToEdit(provider.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "自定义供应商",
                    style = MiuixTheme.textStyles.headline1
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 自定义供应商列表
            items(emptyList<ProviderItem>()) { provider ->
                ProviderItem(
                    name = provider.name,
                    type = provider.type,
                    isConfigured = provider.isConfigured,
                    onClick = { onNavigateToEdit(provider.id) }
                )
            }
        }
    }
}

// 供应商列表项
@Composable
private fun ProviderItem(
    name: String,
    type: String,
    isConfigured: Boolean,
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
                imageVector = when (type) {
                    "openai" -> Icons.Default.SmartToy
                    "google" -> Icons.Default.Language
                    "anthropic" -> Icons.Default.Psychology
                    else -> Icons.Default.Api
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
                    text = if (isConfigured) "已配置" else "未配置",
                    style = MiuixTheme.textStyles.body2,
                    color = if (isConfigured) {
                        MiuixTheme.colorScheme.primary
                    } else {
                        MiuixTheme.colorScheme.onSurfaceVariantSummary
                    }
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

// 供应商数据类
private data class ProviderItem(
    val id: String,
    val name: String,
    val type: String,
    val isConfigured: Boolean
)

// 获取内置供应商列表
private fun getBuiltInProviders(): List<ProviderItem> = listOf(
    ProviderItem("openai", "OpenAI", "openai", false),
    ProviderItem("deepseek", "DeepSeek", "deepseek", false),
    ProviderItem("qwen", "通义千问", "qwen", false),
    ProviderItem("google", "Google Gemini", "google", false),
    ProviderItem("anthropic", "Anthropic Claude", "anthropic", false)
)
