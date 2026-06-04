package com.aichat.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
                Column {
                    SwitchPreference(
                        title = "深色模式",
                        summary = "跟随系统或手动切换",
                        checked = false,
                        onCheckedChange = { /* TODO */ }
                    )
                    ArrowPreference(
                        title = "主题颜色",
                        summary = "跟随系统",
                        onClick = { /* TODO */ }
                    )
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
                Column {
                    ArrowPreference(
                        title = "AI 供应商",
                        summary = "管理 API 配置",
                        onClick = onNavigateToProviders
                    )
                    ArrowPreference(
                        title = "默认模型",
                        summary = "GPT-4o",
                        onClick = { /* TODO */ }
                    )
                    ArrowPreference(
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
                Column {
                    ArrowPreference(
                        title = "备份数据",
                        summary = "导出所有会话和设置",
                        onClick = { /* TODO */ }
                    )
                    ArrowPreference(
                        title = "恢复数据",
                        summary = "从备份文件恢复",
                        onClick = { /* TODO */ }
                    )
                    ArrowPreference(
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
                Column {
                    ArrowPreference(
                        title = "版本",
                        summary = "1.0.0",
                        onClick = { }
                    )
                    ArrowPreference(
                        title = "开源许可",
                        summary = "Apache 2.0",
                        onClick = { /* TODO */ }
                    )
                }
            }
        }
    }
}
