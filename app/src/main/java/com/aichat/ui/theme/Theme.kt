package com.aichat.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController
import top.yukonga.miuix.kmp.theme.ColorSchemeMode

// 应用主题配置
@Composable
fun AiChatTheme(
    content: @Composable () -> Unit
) {
    // 使用 Miuix 主题，支持浅色/深色/Monet 动态取色
    val controller = remember { ThemeController(ColorSchemeMode.System) }
    MiuixTheme(
        controller = controller,
        content = content
    )
}
