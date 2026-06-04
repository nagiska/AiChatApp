package com.aichat.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

// 设置页面
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProviders: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 外观设置
            Text(
                text = "外观",
                style = MiuixTheme.textStyles.headline1
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    var isDarkMode by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "深色模式",
                                style = MiuixTheme.textStyles.body1
                            )
                            Text(
                                text = "跟随系统或手动切换",
                                style = MiuixTheme.textStyles.body2
                            )
                        }
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { isDarkMode = it }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO */ },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "主题颜色",
                                style = MiuixTheme.textStyles.body1
                            )
                            Text(
                                text = "跟随系统",
                                style = MiuixTheme.textStyles.body2
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI 设置
            Text(
                text = "AI 设置",
                style = MiuixTheme.textStyles.headline1
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingsItem(
                        title = "AI 供应商",
                        summary = "管理 API 配置",
                        onClick = onNavigateToProviders
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    SettingsItem(
                        title = "默认模型",
                        summary = "GPT-4o",
                        onClick = { /* TODO */ }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    SettingsItem(
                        title = "默认提示词",
                        summary = "设置系统提示词",
                        onClick = { /* TODO */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 数据设置
            Text(
                text = "数据",
                style = MiuixTheme.textStyles.headline1
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SettingsItem(
                        title = "备份数据",
                        summary = "导出所有会话和设置",
                        onClick = { /* TODO */ }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    SettingsItem(
                        title = "恢复数据",
                        summary = "从备份文件恢复",
                        onClick = { /* TODO */ }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    SettingsItem(
                        title = "清除数据",
                        summary = "删除所有会话和设置",
                        onClick = { /* TODO */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 关于
            Text(
                text = "关于",
                style = MiuixTheme.textStyles.headline1
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "版本",
                            style = MiuixTheme.textStyles.body1
                        )
                        Text(
                            text = "1.0.0",
                            style = MiuixTheme.textStyles.body2
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    SettingsItem(
                        title = "开源许可",
                        summary = "Apache 2.0",
                        onClick = { /* TODO */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    summary: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MiuixTheme.textStyles.body1
            )
            Text(
                text = summary,
                style = MiuixTheme.textStyles.body2
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null
        )
    }
}
